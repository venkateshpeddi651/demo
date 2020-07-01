package com.demo.service;

import com.demo.domain.Facility;
import com.demo.domain.User;
import com.demo.repository.FacilityRepository;
import com.demo.repository.UserRepository;
import com.demo.security.SecurityUtils;
import com.demo.service.dto.FacilityDTO;
import com.demo.service.mapper.FacilityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Facility}.
 */
@Service
@Transactional
public class FacilityService {

    private final Logger log = LoggerFactory.getLogger(FacilityService.class);

    private final FacilityRepository facilityRepository;

    private final FacilityMapper facilityMapper;
    
    private final UserRepository userRepository;

    public FacilityService(FacilityRepository facilityRepository, FacilityMapper facilityMapper, UserRepository userRepository) {
        this.facilityRepository = facilityRepository;
        this.facilityMapper = facilityMapper;
        this.userRepository = userRepository;
    }

    /**
     * Save a facility.
     *
     * @param facilityDTO the entity to save.
     * @return the persisted entity.
     */
    public FacilityDTO save(FacilityDTO facilityDTO) {
        log.debug("Request to save Facility : {}", facilityDTO);
        String login = SecurityUtils.getCurrentUserLogin().get();
        Optional<User> user = userRepository.findOneByLogin(login);
        facilityDTO.setFacilityAdminLogin(login);
        facilityDTO.setFacilityAdminId(user.get().getId());
        Facility facility = facilityMapper.toEntity(facilityDTO);
        facility = facilityRepository.save(facility);
        return facilityMapper.toDto(facility);
    }

    /**
     * Get all the facilities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FacilityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Facilities");
        return facilityRepository.findAll(pageable)
            .map(facilityMapper::toDto);
    }


    /**
     * Get one facility by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacilityDTO> findOne(Long id) {
        log.debug("Request to get Facility : {}", id);
        return facilityRepository.findById(id)
            .map(facilityMapper::toDto);
    }

    /**
     * Delete the facility by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Facility : {}", id);

        facilityRepository.deleteById(id);
    }
}
