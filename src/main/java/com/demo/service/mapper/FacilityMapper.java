package com.demo.service.mapper;


import com.demo.domain.*;
import com.demo.service.dto.FacilityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Facility} and its DTO {@link FacilityDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface FacilityMapper extends EntityMapper<FacilityDTO, Facility> {

    @Mapping(source = "facilityAdmin.id", target = "facilityAdminId")
    @Mapping(source = "facilityAdmin.login", target = "facilityAdminLogin")
    FacilityDTO toDto(Facility facility);

    @Mapping(source = "facilityAdminId", target = "facilityAdmin")
    Facility toEntity(FacilityDTO facilityDTO);

    default Facility fromId(Long id) {
        if (id == null) {
            return null;
        }
        Facility facility = new Facility();
        facility.setId(id);
        return facility;
    }
}
