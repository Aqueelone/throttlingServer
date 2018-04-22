package online.foursstudio.throttlingserver.service.mapper;

import online.foursstudio.throttlingserver.domain.*;
import online.foursstudio.throttlingserver.service.dto.SLADTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SLA and its DTO SLADTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface SLAMapper extends EntityMapper<SLADTO, SLA> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    SLADTO toDto(SLA sLA);

    @Mapping(source = "userId", target = "user")
    SLA toEntity(SLADTO sLADTO);

    default SLA fromId(Long id) {
        if (id == null) {
            return null;
        }
        SLA sLA = new SLA();
        sLA.setId(id);
        return sLA;
    }
}
