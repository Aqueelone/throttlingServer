package online.foursstudio.throttlingserver.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SLA entity.
 */
public class SLADTO implements Serializable {

    private Long id;

    private Integer rps;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRps() {
        return rps;
    }

    public void setRps(Integer rps) {
        this.rps = rps;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SLADTO sLADTO = (SLADTO) o;
        if (sLADTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sLADTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SLADTO{" +
            "id=" + getId() +
            ", rps=" + getRps() +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            "}";
    }
}
