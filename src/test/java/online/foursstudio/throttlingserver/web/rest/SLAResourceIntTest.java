package online.foursstudio.throttlingserver.web.rest;

import online.foursstudio.throttlingserver.ThrottlingServiceApp;

import online.foursstudio.throttlingserver.domain.SLA;
import online.foursstudio.throttlingserver.domain.User;
import online.foursstudio.throttlingserver.repository.SLARepository;
import online.foursstudio.throttlingserver.service.SLAService;
import online.foursstudio.throttlingserver.service.dto.SLADTO;
import online.foursstudio.throttlingserver.service.mapper.SLAMapper;
import online.foursstudio.throttlingserver.web.rest.errors.ExceptionTranslator;
import online.foursstudio.throttlingserver.service.dto.SLACriteria;
import online.foursstudio.throttlingserver.service.SLAQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.ArrayList;

import static online.foursstudio.throttlingserver.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SLAResource REST controller.
 *
 * @see SLAResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThrottlingServiceApp.class)
public class SLAResourceIntTest {

    private static final Integer DEFAULT_RPS = 1;
    private static final Integer UPDATED_RPS = 2;

    @Autowired
    private SLARepository sLARepository;



    @Autowired
    private SLAMapper sLAMapper;
    

    @Autowired
    private SLAService sLAService;

    @Autowired
    private SLAQueryService sLAQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSLAMockMvc;

    private SLA sLA;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SLAResource sLAResource = new SLAResource(sLAService, sLAQueryService);
        this.restSLAMockMvc = MockMvcBuilders.standaloneSetup(sLAResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SLA createEntity(EntityManager em) {
        SLA sLA = new SLA()
            .rps(DEFAULT_RPS);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        sLA.setUser(user);
        return sLA;
    }

    @Before
    public void initTest() {
        sLA = createEntity(em);
    }

    @Test
    @Transactional
    public void createSLA() throws Exception {
        int databaseSizeBeforeCreate = sLARepository.findAll().size();

        // Create the SLA
        SLADTO sLADTO = sLAMapper.toDto(sLA);
        restSLAMockMvc.perform(post("/api/slas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sLADTO)))
            .andExpect(status().isCreated());

        // Validate the SLA in the database
        List<SLA> sLAList = sLARepository.findAll();
        assertThat(sLAList).hasSize(databaseSizeBeforeCreate + 1);
        SLA testSLA = sLAList.get(sLAList.size() - 1);
        assertThat(testSLA.getRps()).isEqualTo(DEFAULT_RPS);
    }

    @Test
    @Transactional
    public void createSLAWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sLARepository.findAll().size();

        // Create the SLA with an existing ID
        sLA.setId(1L);
        SLADTO sLADTO = sLAMapper.toDto(sLA);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSLAMockMvc.perform(post("/api/slas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sLADTO)))
            .andExpect(status().isBadRequest());

        // Validate the SLA in the database
        List<SLA> sLAList = sLARepository.findAll();
        assertThat(sLAList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSLAS() throws Exception {
        // Initialize the database
        sLARepository.saveAndFlush(sLA);

        // Get all the sLAList
        restSLAMockMvc.perform(get("/api/slas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sLA.getId().intValue())))
            .andExpect(jsonPath("$.[*].rps").value(hasItem(DEFAULT_RPS)));
    }
    

    @Test
    @Transactional
    public void getSLA() throws Exception {
        // Initialize the database
        sLARepository.saveAndFlush(sLA);

        // Get the sLA
        restSLAMockMvc.perform(get("/api/slas/{id}", sLA.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sLA.getId().intValue()))
            .andExpect(jsonPath("$.rps").value(DEFAULT_RPS));
    }

    @Test
    @Transactional
    public void getAllSLASByRpsIsEqualToSomething() throws Exception {
        // Initialize the database
        sLARepository.saveAndFlush(sLA);

        // Get all the sLAList where rps equals to DEFAULT_RPS
        defaultSLAShouldBeFound("rps.equals=" + DEFAULT_RPS);

        // Get all the sLAList where rps equals to UPDATED_RPS
        defaultSLAShouldNotBeFound("rps.equals=" + UPDATED_RPS);
    }

    @Test
    @Transactional
    public void getAllSLASByRpsIsInShouldWork() throws Exception {
        // Initialize the database
        sLARepository.saveAndFlush(sLA);

        // Get all the sLAList where rps in DEFAULT_RPS or UPDATED_RPS
        defaultSLAShouldBeFound("rps.in=" + DEFAULT_RPS + "," + UPDATED_RPS);

        // Get all the sLAList where rps equals to UPDATED_RPS
        defaultSLAShouldNotBeFound("rps.in=" + UPDATED_RPS);
    }

    @Test
    @Transactional
    public void getAllSLASByRpsIsNullOrNotNull() throws Exception {
        // Initialize the database
        sLARepository.saveAndFlush(sLA);

        // Get all the sLAList where rps is not null
        defaultSLAShouldBeFound("rps.specified=true");

        // Get all the sLAList where rps is null
        defaultSLAShouldNotBeFound("rps.specified=false");
    }

    @Test
    @Transactional
    public void getAllSLASByRpsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sLARepository.saveAndFlush(sLA);

        // Get all the sLAList where rps greater than or equals to DEFAULT_RPS
        defaultSLAShouldBeFound("rps.greaterOrEqualThan=" + DEFAULT_RPS);

        // Get all the sLAList where rps greater than or equals to UPDATED_RPS
        defaultSLAShouldNotBeFound("rps.greaterOrEqualThan=" + UPDATED_RPS);
    }

    @Test
    @Transactional
    public void getAllSLASByRpsIsLessThanSomething() throws Exception {
        // Initialize the database
        sLARepository.saveAndFlush(sLA);

        // Get all the sLAList where rps less than or equals to DEFAULT_RPS
        defaultSLAShouldNotBeFound("rps.lessThan=" + DEFAULT_RPS);

        // Get all the sLAList where rps less than or equals to UPDATED_RPS
        defaultSLAShouldBeFound("rps.lessThan=" + UPDATED_RPS);
    }


    @Test
    @Transactional
    public void getAllSLASByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        sLA.setUser(user);
        sLARepository.saveAndFlush(sLA);
        Long userId = user.getId();

        // Get all the sLAList where user equals to userId
        defaultSLAShouldBeFound("userId.equals=" + userId);

        // Get all the sLAList where user equals to userId + 1
        defaultSLAShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSLAShouldBeFound(String filter) throws Exception {
        restSLAMockMvc.perform(get("/api/slas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sLA.getId().intValue())))
            .andExpect(jsonPath("$.[*].rps").value(hasItem(DEFAULT_RPS)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSLAShouldNotBeFound(String filter) throws Exception {
        restSLAMockMvc.perform(get("/api/slas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSLA() throws Exception {
        // Get the sLA
        restSLAMockMvc.perform(get("/api/slas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSLA() throws Exception {
        // Initialize the database
        sLARepository.saveAndFlush(sLA);

        int databaseSizeBeforeUpdate = sLARepository.findAll().size();

        // Update the sLA
        SLA updatedSLA = sLARepository.findById(sLA.getId()).get();
        // Disconnect from session so that the updates on updatedSLA are not directly saved in db
        em.detach(updatedSLA);
        updatedSLA
            .rps(UPDATED_RPS);
        SLADTO sLADTO = sLAMapper.toDto(updatedSLA);

        restSLAMockMvc.perform(put("/api/slas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sLADTO)))
            .andExpect(status().isOk());

        // Validate the SLA in the database
        List<SLA> sLAList = sLARepository.findAll();
        assertThat(sLAList).hasSize(databaseSizeBeforeUpdate);
        SLA testSLA = sLAList.get(sLAList.size() - 1);
        assertThat(testSLA.getRps()).isEqualTo(UPDATED_RPS);
    }

    @Test
    @Transactional
    public void updateNonExistingSLA() throws Exception {
        int databaseSizeBeforeUpdate = sLARepository.findAll().size();

        // Create the SLA
        SLADTO sLADTO = sLAMapper.toDto(sLA);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSLAMockMvc.perform(put("/api/slas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sLADTO)))
            .andExpect(status().isCreated());

        // Validate the SLA in the database
        List<SLA> sLAList = sLARepository.findAll();
        assertThat(sLAList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSLA() throws Exception {
        // Initialize the database
        sLARepository.saveAndFlush(sLA);

        int databaseSizeBeforeDelete = sLARepository.findAll().size();

        // Get the sLA
        restSLAMockMvc.perform(delete("/api/slas/{id}", sLA.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SLA> sLAList = sLARepository.findAll();
        assertThat(sLAList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SLA.class);
        SLA sLA1 = new SLA();
        sLA1.setId(1L);
        SLA sLA2 = new SLA();
        sLA2.setId(sLA1.getId());
        assertThat(sLA1).isEqualTo(sLA2);
        sLA2.setId(2L);
        assertThat(sLA1).isNotEqualTo(sLA2);
        sLA1.setId(null);
        assertThat(sLA1).isNotEqualTo(sLA2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SLADTO.class);
        SLADTO sLADTO1 = new SLADTO();
        sLADTO1.setId(1L);
        SLADTO sLADTO2 = new SLADTO();
        assertThat(sLADTO1).isNotEqualTo(sLADTO2);
        sLADTO2.setId(sLADTO1.getId());
        assertThat(sLADTO1).isEqualTo(sLADTO2);
        sLADTO2.setId(2L);
        assertThat(sLADTO1).isNotEqualTo(sLADTO2);
        sLADTO1.setId(null);
        assertThat(sLADTO1).isNotEqualTo(sLADTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sLAMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sLAMapper.fromId(null)).isNull();
    }
}
