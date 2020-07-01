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

import com.demo.domain.Resident;
import com.demo.domain.*; // for static metamodels
import com.demo.repository.ResidentRepository;
import com.demo.service.dto.ResidentCriteria;
import com.demo.service.dto.ResidentDTO;
import com.demo.service.mapper.ResidentMapper;

/**
 * Service for executing complex queries for {@link Resident} entities in the database.
 * The main input is a {@link ResidentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ResidentDTO} or a {@link Page} of {@link ResidentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ResidentQueryService extends QueryService<Resident> {

    private final Logger log = LoggerFactory.getLogger(ResidentQueryService.class);

    private final ResidentRepository residentRepository;

    private final ResidentMapper residentMapper;

    public ResidentQueryService(ResidentRepository residentRepository, ResidentMapper residentMapper) {
        this.residentRepository = residentRepository;
        this.residentMapper = residentMapper;
    }

    /**
     * Return a {@link List} of {@link ResidentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ResidentDTO> findByCriteria(ResidentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Resident> specification = createSpecification(criteria);
        return residentMapper.toDto(residentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ResidentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ResidentDTO> findByCriteria(ResidentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Resident> specification = createSpecification(criteria);
        return residentRepository.findAll(specification, page)
            .map(residentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ResidentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Resident> specification = createSpecification(criteria);
        return residentRepository.count(specification);
    }

    /**
     * Function to convert {@link ResidentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Resident> createSpecification(ResidentCriteria criteria) {
        Specification<Resident> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Resident_.id));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Resident_.phone));
            }
            if (criteria.getRoomId() != null) {
                specification = specification.and(buildSpecification(criteria.getRoomId(),
                    root -> root.join(Resident_.room, JoinType.LEFT).get(Room_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Resident_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
