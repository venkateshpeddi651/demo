package com.demo.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.demo.domain.Facility;
import com.demo.domain.*; // for static metamodels
import com.demo.repository.FacilityRepository;
import com.demo.service.dto.FacilityCriteria;
import com.demo.service.dto.FacilityDTO;
import com.demo.service.mapper.FacilityMapper;

/**
 * Service for executing complex queries for {@link Facility} entities in the database.
 * The main input is a {@link FacilityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FacilityDTO} or a {@link Page} of {@link FacilityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacilityQueryService extends QueryService<Facility> {

    private final Logger log = LoggerFactory.getLogger(FacilityQueryService.class);

    private final FacilityRepository facilityRepository;

    private final FacilityMapper facilityMapper;

    public FacilityQueryService(FacilityRepository facilityRepository, FacilityMapper facilityMapper) {
        this.facilityRepository = facilityRepository;
        this.facilityMapper = facilityMapper;
    }

    /**
     * Return a {@link List} of {@link FacilityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FacilityDTO> findByCriteria(FacilityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Facility> specification = createSpecification(criteria);
        return facilityMapper.toDto(facilityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FacilityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FacilityDTO> findByCriteria(FacilityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Facility> specification = createSpecification(criteria);
        return facilityRepository.findAll(specification, page)
            .map(facilityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacilityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Facility> specification = createSpecification(criteria);
        return facilityRepository.count(specification);
    }

    /**
     * Function to convert {@link FacilityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Facility> createSpecification(FacilityCriteria criteria) {
        Specification<Facility> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Facility_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Facility_.name));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Facility_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Facility_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Facility_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Facility_.lastModifiedDate));
            }
            if (criteria.getFacilityAdminId() != null) {
                specification = specification.and(buildSpecification(criteria.getFacilityAdminId(),
                    root -> root.join(Facility_.facilityAdmin, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
