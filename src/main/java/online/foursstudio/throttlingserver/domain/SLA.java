package online.foursstudio.throttlingserver.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A SLA.
 */
@Entity
@Table(name = "sla")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SLA implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "rps")
    private Integer rps;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRps() {
        return rps;
    }

    public SLA rps(Integer rps) {
        this.rps = rps;
        return this;
    }

    public void setRps(Integer rps) {
        this.rps = rps;
    }

    public User getUser() {
        return user;
    }

    public SLA user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SLA sLA = (SLA) o;
        if (sLA.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sLA.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SLA{" +
            "id=" + getId() +
            ", rps=" + getRps() +
            "}";
    }
}
