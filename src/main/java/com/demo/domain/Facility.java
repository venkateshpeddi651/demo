package com.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Facility.
 */
@Entity
@Table(name = "facility")
public class Facility extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "facilities", allowSetters = true)
    private User facilityAdmin;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Facility name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Facility createdBy(String createdBy) {
        super.setCreatedBy(createdBy);
        return this;
    }

    public Facility createdDate(Instant createdDate) {
        super.setCreatedDate(createdDate); 
        return this;
    }

    public Facility lastModifiedBy(String lastModifiedBy) {
        super.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public Facility lastModifiedDate(Instant lastModifiedDate) {
        super.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public User getFacilityAdmin() {
        return facilityAdmin;
    }

    public Facility facilityAdmin(User user) {
        this.facilityAdmin = user;
        return this;
    }

    public void setFacilityAdmin(User user) {
        this.facilityAdmin = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facility)) {
            return false;
        }
        return id != null && id.equals(((Facility) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facility{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
