package online.foursstudio.throttlingserver.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import online.foursstudio.throttlingserver.domain.SLA;
import online.foursstudio.throttlingserver.domain.*; // for static metamodels
import online.foursstudio.throttlingserver.repository.SLARepository;
import online.foursstudio.throttlingserver.service.dto.SLACriteria;

import online.foursstudio.throttlingserver.service.dto.SLADTO;
import online.foursstudio.throttlingserver.service.mapper.SLAMapper;

/**
 * Service for executing complex queries for SLA entities in the database.
 * The main input is a {@link SLACriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SLADTO} or a {@link Page} of {@link SLADTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SLAQueryService extends QueryService<SLA> {

    private final Logger log = LoggerFactory.getLogger(SLAQueryService.class);

    private final SLARepository sLARepository;

    private final SLAMapper sLAMapper;

    public SLAQueryService(SLARepository sLARepository, SLAMapper sLAMapper) {
        this.sLARepository = sLARepository;
        this.sLAMapper = sLAMapper;
    }

    /**
     * Return a {@link List} of {@link SLADTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SLADTO> findByCriteria(SLACriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SLA> specification = createSpecification(criteria);
        return sLAMapper.toDto(sLARepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SLADTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SLADTO> findByCriteria(SLACriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SLA> specification = createSpecification(criteria);
        return sLARepository.findAll(specification, page)
            .map(sLAMapper::toDto);
    }

    /**
     * Function to convert SLACriteria to a {@link Specification}
     */
    private Specification<SLA> createSpecification(SLACriteria criteria) {
        Specification<SLA> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SLA_.id));
            }
            if (criteria.getRps() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRps(), SLA_.rps));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), SLA_.user, User_.id));
            }
        }
        return specification;
    }

}
