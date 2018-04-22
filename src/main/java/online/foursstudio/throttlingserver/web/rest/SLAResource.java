package online.foursstudio.throttlingserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import online.foursstudio.throttlingserver.service.SLAService;
import online.foursstudio.throttlingserver.web.rest.errors.BadRequestAlertException;
import online.foursstudio.throttlingserver.web.rest.util.HeaderUtil;
import online.foursstudio.throttlingserver.service.dto.SLADTO;
import online.foursstudio.throttlingserver.service.dto.SLACriteria;
import online.foursstudio.throttlingserver.service.SLAQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SLA.
 */
@RestController
@RequestMapping("/api")
public class SLAResource {

    private final Logger log = LoggerFactory.getLogger(SLAResource.class);

    private static final String ENTITY_NAME = "sLA";

    private final SLAService sLAService;

    private final SLAQueryService sLAQueryService;

    public SLAResource(SLAService sLAService, SLAQueryService sLAQueryService) {
        this.sLAService = sLAService;
        this.sLAQueryService = sLAQueryService;
    }

    /**
     * POST  /slas : Create a new sLA.
     *
     * @param sLADTO the sLADTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sLADTO, or with status 400 (Bad Request) if the sLA has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/slas")
    @Timed
    public ResponseEntity<SLADTO> createSLA(@Valid @RequestBody SLADTO sLADTO) throws URISyntaxException {
        log.debug("REST request to save SLA : {}", sLADTO);
        if (sLADTO.getId() != null) {
            throw new BadRequestAlertException("A new sLA cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SLADTO result = sLAService.save(sLADTO);
        return ResponseEntity.created(new URI("/api/slas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /slas : Updates an existing sLA.
     *
     * @param sLADTO the sLADTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sLADTO,
     * or with status 400 (Bad Request) if the sLADTO is not valid,
     * or with status 500 (Internal Server Error) if the sLADTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/slas")
    @Timed
    public ResponseEntity<SLADTO> updateSLA(@Valid @RequestBody SLADTO sLADTO) throws URISyntaxException {
        log.debug("REST request to update SLA : {}", sLADTO);
        if (sLADTO.getId() == null) {
            return createSLA(sLADTO);
        }
        SLADTO result = sLAService.save(sLADTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sLADTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /slas : get all the sLAS.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of sLAS in body
     */
    @GetMapping("/slas")
    @Timed
    public ResponseEntity<List<SLADTO>> getAllSLAS(SLACriteria criteria) {
        log.debug("REST request to get SLAS by criteria: {}", criteria);
        List<SLADTO> entityList = sLAQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /slas/:id : get the "id" sLA.
     *
     * @param id the id of the sLADTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sLADTO, or with status 404 (Not Found)
     */
    @GetMapping("/slas/{id}")
    @Timed
    public ResponseEntity<SLADTO> getSLA(@PathVariable Long id) {
        log.debug("REST request to get SLA : {}", id);
        Optional<SLADTO> sLADTO = sLAService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sLADTO);
    }

    /**
     * DELETE  /slas/:id : delete the "id" sLA.
     *
     * @param id the id of the sLADTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/slas/{id}")
    @Timed
    public ResponseEntity<Void> deleteSLA(@PathVariable Long id) {
        log.debug("REST request to delete SLA : {}", id);
        sLAService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
