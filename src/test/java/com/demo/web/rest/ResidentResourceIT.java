package com.demo.web.rest;

import com.demo.DemoApp;
import com.demo.domain.Resident;
import com.demo.domain.Room;
import com.demo.domain.User;
import com.demo.repository.ResidentRepository;
import com.demo.service.ResidentService;
import com.demo.service.dto.ResidentDTO;
import com.demo.service.mapper.ResidentMapper;
import com.demo.service.dto.ResidentCriteria;
import com.demo.service.ResidentQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ResidentResource} REST controller.
 */
@SpringBootTest(classes = DemoApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ResidentResourceIT {

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private ResidentMapper residentMapper;

    @Autowired
    private ResidentService residentService;

    @Autowired
    private ResidentQueryService residentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResidentMockMvc;

    private Resident resident;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resident createEntity(EntityManager em) {
        Resident resident = new Resident()
            .phone(DEFAULT_PHONE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        resident.setUser(user);
        return resident;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resident createUpdatedEntity(EntityManager em) {
        Resident resident = new Resident()
            .phone(UPDATED_PHONE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        resident.setUser(user);
        return resident;
    }

    @BeforeEach
    public void initTest() {
        resident = createEntity(em);
    }

    @Test
    @Transactional
    public void createResident() throws Exception {
        int databaseSizeBeforeCreate = residentRepository.findAll().size();
        // Create the Resident
        ResidentDTO residentDTO = residentMapper.toDto(resident);
        restResidentMockMvc.perform(post("/api/residents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(residentDTO)))
            .andExpect(status().isCreated());

        // Validate the Resident in the database
        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeCreate + 1);
        Resident testResident = residentList.get(residentList.size() - 1);
        assertThat(testResident.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void createResidentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = residentRepository.findAll().size();

        // Create the Resident with an existing ID
        resident.setId(1L);
        ResidentDTO residentDTO = residentMapper.toDto(resident);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResidentMockMvc.perform(post("/api/residents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(residentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Resident in the database
        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = residentRepository.findAll().size();
        // set the field null
        resident.setPhone(null);

        // Create the Resident, which fails.
        ResidentDTO residentDTO = residentMapper.toDto(resident);


        restResidentMockMvc.perform(post("/api/residents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(residentDTO)))
            .andExpect(status().isBadRequest());

        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResidents() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        // Get all the residentList
        restResidentMockMvc.perform(get("/api/residents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resident.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }
    
    @Test
    @Transactional
    public void getResident() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        // Get the resident
        restResidentMockMvc.perform(get("/api/residents/{id}", resident.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resident.getId().intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }


    @Test
    @Transactional
    public void getResidentsByIdFiltering() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        Long id = resident.getId();

        defaultResidentShouldBeFound("id.equals=" + id);
        defaultResidentShouldNotBeFound("id.notEquals=" + id);

        defaultResidentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultResidentShouldNotBeFound("id.greaterThan=" + id);

        defaultResidentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultResidentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllResidentsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        // Get all the residentList where phone equals to DEFAULT_PHONE
        defaultResidentShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the residentList where phone equals to UPDATED_PHONE
        defaultResidentShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllResidentsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        // Get all the residentList where phone not equals to DEFAULT_PHONE
        defaultResidentShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the residentList where phone not equals to UPDATED_PHONE
        defaultResidentShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllResidentsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        // Get all the residentList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultResidentShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the residentList where phone equals to UPDATED_PHONE
        defaultResidentShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllResidentsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        // Get all the residentList where phone is not null
        defaultResidentShouldBeFound("phone.specified=true");

        // Get all the residentList where phone is null
        defaultResidentShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllResidentsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        // Get all the residentList where phone contains DEFAULT_PHONE
        defaultResidentShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the residentList where phone contains UPDATED_PHONE
        defaultResidentShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllResidentsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        // Get all the residentList where phone does not contain DEFAULT_PHONE
        defaultResidentShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the residentList where phone does not contain UPDATED_PHONE
        defaultResidentShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllResidentsByRoomIsEqualToSomething() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);
        Room room = RoomResourceIT.createEntity(em);
        em.persist(room);
        em.flush();
        resident.setRoom(room);
        residentRepository.saveAndFlush(resident);
        Long roomId = room.getId();

        // Get all the residentList where room equals to roomId
        defaultResidentShouldBeFound("roomId.equals=" + roomId);

        // Get all the residentList where room equals to roomId + 1
        defaultResidentShouldNotBeFound("roomId.equals=" + (roomId + 1));
    }


    @Test
    @Transactional
    public void getAllResidentsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = resident.getUser();
        residentRepository.saveAndFlush(resident);
        Long userId = user.getId();

        // Get all the residentList where user equals to userId
        defaultResidentShouldBeFound("userId.equals=" + userId);

        // Get all the residentList where user equals to userId + 1
        defaultResidentShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultResidentShouldBeFound(String filter) throws Exception {
        restResidentMockMvc.perform(get("/api/residents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resident.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));

        // Check, that the count call also returns 1
        restResidentMockMvc.perform(get("/api/residents/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultResidentShouldNotBeFound(String filter) throws Exception {
        restResidentMockMvc.perform(get("/api/residents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restResidentMockMvc.perform(get("/api/residents/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingResident() throws Exception {
        // Get the resident
        restResidentMockMvc.perform(get("/api/residents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResident() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        int databaseSizeBeforeUpdate = residentRepository.findAll().size();

        // Update the resident
        Resident updatedResident = residentRepository.findById(resident.getId()).get();
        // Disconnect from session so that the updates on updatedResident are not directly saved in db
        em.detach(updatedResident);
        updatedResident
            .phone(UPDATED_PHONE);
        ResidentDTO residentDTO = residentMapper.toDto(updatedResident);

        restResidentMockMvc.perform(put("/api/residents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(residentDTO)))
            .andExpect(status().isOk());

        // Validate the Resident in the database
        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeUpdate);
        Resident testResident = residentList.get(residentList.size() - 1);
        assertThat(testResident.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void updateNonExistingResident() throws Exception {
        int databaseSizeBeforeUpdate = residentRepository.findAll().size();

        // Create the Resident
        ResidentDTO residentDTO = residentMapper.toDto(resident);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResidentMockMvc.perform(put("/api/residents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(residentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Resident in the database
        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteResident() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        int databaseSizeBeforeDelete = residentRepository.findAll().size();

        // Delete the resident
        restResidentMockMvc.perform(delete("/api/residents/{id}", resident.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
