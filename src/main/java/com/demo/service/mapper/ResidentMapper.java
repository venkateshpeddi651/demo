package com.demo.service.mapper;


import com.demo.domain.*;
import com.demo.service.dto.ResidentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Resident} and its DTO {@link ResidentDTO}.
 */
@Mapper(componentModel = "spring", uses = {RoomMapper.class, UserMapper.class})
public interface ResidentMapper extends EntityMapper<ResidentDTO, Resident> {

    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    ResidentDTO toDto(Resident resident);

    @Mapping(source = "roomId", target = "room")
    @Mapping(source = "userId", target = "user")
    Resident toEntity(ResidentDTO residentDTO);

    default Resident fromId(Long id) {
        if (id == null) {
            return null;
        }
        Resident resident = new Resident();
        resident.setId(id);
        return resident;
    }
}
