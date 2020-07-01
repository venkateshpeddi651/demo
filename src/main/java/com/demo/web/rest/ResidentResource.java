package com.demo.web.rest;

import com.demo.service.ResidentService;
import com.demo.web.rest.errors.BadRequestAlertException;
import com.demo.service.dto.ResidentDTO;
import com.demo.service.dto.UserDTO;
import com.demo.service.dto.ResidentCriteria;
import com.demo.service.ResidentQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.demo.domain.Resident}.
 */
@RestController
@RequestMapping("/api")
public class ResidentResource {

    private final Logger log = LoggerFactory.getLogger(ResidentResource.class);

    private static final String ENTITY_NAME = "resident";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResidentService residentService;

    private final ResidentQueryService residentQueryService;
    
    public ResidentResource(ResidentService residentService, ResidentQueryService residentQueryService) {
        this.residentService = residentService;
        this.residentQueryService = residentQueryService;
    }

    /**
     * {@code POST  /residents} : Create a new resident.
     *
     * @param residentDTO the residentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new residentDTO, or with status {@code 400 (Bad Request)} if the resident has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/residents")
    public ResponseEntity<ResidentDTO> createResident(@Valid @RequestBody ResidentDTO residentDTO) throws URISyntaxException {
        log.debug("REST request to save Resident : {}", residentDTO);
        if (residentDTO.getId() != null) {
            throw new BadRequestAlertException("A new resident cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(residentDTO.getUserId())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        ResidentDTO result = residentService.save(residentDTO);
        return ResponseEntity.created(new URI("/api/residents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /residents} : Updates an existing resident.
     *
     * @param residentDTO the residentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated residentDTO,
     * or with status {@code 400 (Bad Request)} if the residentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the residentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/residents")
    public ResponseEntity<ResidentDTO> updateResident(@Valid @RequestBody ResidentDTO residentDTO) throws URISyntaxException {
        log.debug("REST request to update Resident : {}", residentDTO);
        if (residentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResidentDTO result = residentService.save(residentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, residentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /residents} : get all the residents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of residents in body.
     */
    @GetMapping("/residents")
    public ResponseEntity<List<ResidentDTO>> getAllResidents(ResidentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Residents by criteria: {}", criteria);
        Page<ResidentDTO> page = residentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    
    @GetMapping("/residents/unassigned")
    public List<UserDTO> getAllUnAssignedUsers() {
        log.debug("REST request to get unassigned Residents ");
        return residentService.findAllUnAssignedUsers();
    }

    /**
     * {@code GET  /residents/count} : count all the residents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/residents/count")
    public ResponseEntity<Long> countResidents(ResidentCriteria criteria) {
        log.debug("REST request to count Residents by criteria: {}", criteria);
        return ResponseEntity.ok().body(residentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /residents/:id} : get the "id" resident.
     *
     * @param id the id of the residentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the residentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/residents/{id}")
    public ResponseEntity<ResidentDTO> getResident(@PathVariable Long id) {
        log.debug("REST request to get Resident : {}", id);
        Optional<ResidentDTO> residentDTO = residentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(residentDTO);
    }

    /**
     * {@code DELETE  /residents/:id} : delete the "id" resident.
     *
     * @param id the id of the residentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/residents/{id}")
    public ResponseEntity<Void> deleteResident(@PathVariable Long id) {
        log.debug("REST request to delete Resident : {}", id);

        residentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
