package com.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.domain.Resident;
import com.demo.repository.ResidentRepository;
import com.demo.repository.UserRepository;
import com.demo.service.dto.ResidentDTO;
import com.demo.service.dto.UserDTO;
import com.demo.service.mapper.ResidentMapper;
import com.demo.service.mapper.UserMapper;

/**
 * Service Implementation for managing {@link Resident}.
 */
@Service
@Transactional
public class ResidentService {

    private final Logger log = LoggerFactory.getLogger(ResidentService.class);

    private final ResidentRepository residentRepository;

    private final ResidentMapper residentMapper;

    private final UserRepository userRepository;
    
    private final UserMapper userMapper;

    public ResidentService(ResidentRepository residentRepository, ResidentMapper residentMapper, 
    		UserRepository userRepository, UserMapper userMapper) {
        this.residentRepository = residentRepository;
        this.residentMapper = residentMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Save a resident.
     *
     * @param residentDTO the entity to save.
     * @return the persisted entity.
     */
    public ResidentDTO save(ResidentDTO residentDTO) {
        log.debug("Request to save Resident : {}", residentDTO);
        Resident resident = residentMapper.toEntity(residentDTO);
        Long userId = residentDTO.getUserId();
        userRepository.findById(userId).ifPresent(resident::user);
        resident = residentRepository.save(resident);
        return residentMapper.toDto(resident);
    }

    /**
     * Get all the residents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ResidentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Residents");
        return residentRepository.findAll(pageable)
            .map(residentMapper::toDto);
    }


    /**
     * Get one resident by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ResidentDTO> findOne(Long id) {
        log.debug("Request to get Resident : {}", id);
        return residentRepository.findById(id)
            .map(residentMapper::toDto);
    }

    /**
     * Delete the resident by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Resident : {}", id);

        residentRepository.deleteById(id);
    }

	public List<UserDTO> findAllUnAssignedUsers() {
		return userMapper.usersToUserDTOs(userRepository.findAllUnAssignedUsers());
	}
}
