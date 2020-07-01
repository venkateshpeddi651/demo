package com.demo.service.mapper;


import com.demo.domain.*;
import com.demo.service.dto.RoomDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Room} and its DTO {@link RoomDTO}.
 */
@Mapper(componentModel = "spring", uses = {FacilityMapper.class})
public interface RoomMapper extends EntityMapper<RoomDTO, Room> {

    @Mapping(source = "facility.id", target = "facilityId")
    @Mapping(source = "facility.facilityAdmin.login", target = "facilityAdminLogin")
    RoomDTO toDto(Room room);

    @Mapping(source = "facilityId", target = "facility")
    Room toEntity(RoomDTO roomDTO);

    default Room fromId(Long id) {
        if (id == null) {
            return null;
        }
        Room room = new Room();
        room.setId(id);
        return room;
    }
}
