package online.foursstudio.throttlingserver.service.impl;

import online.foursstudio.throttlingserver.service.SLAService;
import online.foursstudio.throttlingserver.domain.SLA;
import online.foursstudio.throttlingserver.repository.SLARepository;
import online.foursstudio.throttlingserver.service.dto.SLADTO;
import online.foursstudio.throttlingserver.service.mapper.SLAMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing SLA.
 */
@Service
@Transactional
public class SLAServiceImpl implements SLAService {

    private final Logger log = LoggerFactory.getLogger(SLAServiceImpl.class);

    private final SLARepository sLARepository;

    private final SLAMapper sLAMapper;

    public SLAServiceImpl(SLARepository sLARepository, SLAMapper sLAMapper) {
        this.sLARepository = sLARepository;
        this.sLAMapper = sLAMapper;
    }

    /**
     * Save a sLA.
     *
     * @param sLADTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SLADTO save(SLADTO sLADTO) {
        log.debug("Request to save SLA : {}", sLADTO);
        SLA sLA = sLAMapper.toEntity(sLADTO);
        sLA = sLARepository.save(sLA);
        return sLAMapper.toDto(sLA);
    }

    /**
     * Get all the sLAS.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SLADTO> findAll() {
        log.debug("Request to get all SLAS");
        return sLARepository.findAll().stream()
            .map(sLAMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one sLA by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SLADTO> findOne(Long id) {
        log.debug("Request to get SLA : {}", id);
        return sLARepository.findById(id)
            .map(sLAMapper::toDto);
    }

    /**
     * Delete the sLA by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SLA : {}", id);
        sLARepository.deleteById(id);
    }
}
