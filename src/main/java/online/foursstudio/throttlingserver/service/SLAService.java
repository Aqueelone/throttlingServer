package online.foursstudio.throttlingserver.service;

import online.foursstudio.throttlingserver.service.dto.SLADTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing SLA.
 */
public interface SLAService {

    /**
     * Save a sLA.
     *
     * @param sLADTO the entity to save
     * @return the persisted entity
     */
    SLADTO save(SLADTO sLADTO);

    /**
     * Get all the sLAS.
     *
     * @return the list of entities
     */
    List<SLADTO> findAll();


    /**
     * Get the "id" sLA.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SLADTO> findOne(Long id);

    /**
     * Delete the "id" sLA.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
