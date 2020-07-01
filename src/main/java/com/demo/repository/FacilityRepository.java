package com.demo.repository;

import com.demo.domain.Facility;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Facility entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long>, JpaSpecificationExecutor<Facility> {

    @Query("select facility from Facility facility where facility.facilityAdmin.login = ?#{principal.username}")
    List<Facility> findByFacilityAdminIsCurrentUser();
}
