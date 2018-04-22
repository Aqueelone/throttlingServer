package online.foursstudio.throttlingserver.repository;

import online.foursstudio.throttlingserver.domain.SLA;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the SLA entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SLARepository extends JpaRepository<SLA, Long>, JpaSpecificationExecutor<SLA> {

}
