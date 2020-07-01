package com.demo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.demo.DemoApp;
import com.demo.domain.Facility;
import com.demo.domain.Room;
import com.demo.repository.RoomRepository;
import com.demo.service.dto.RoomDTO;
import com.demo.service.mapper.RoomMapper;

/**
 * Integration tests for the {@link RoomResource} REST controller.
 */
@SpringBootTest(classes = DemoApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RoomResourceIT {

    private static final String DEFAULT_ROOM_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";


    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomMockMvc;

    private Room room;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createEntity(EntityManager em) {
        Room room = new Room()
            .roomNumber(DEFAULT_ROOM_NUMBER)
            .name(DEFAULT_NAME);
        // Add required entity
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facility = FacilityResourceIT.createEntity(em);
            em.persist(facility);
            em.flush();
        } else {
            facility = TestUtil.findAll(em, Facility.class).get(0);
        }
        room.setFacility(facility);
        return room;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createUpdatedEntity(EntityManager em) {
        Room room = new Room()
            .roomNumber(UPDATED_ROOM_NUMBER)
            .name(UPDATED_NAME);
        // Add required entity
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facility = FacilityResourceIT.createUpdatedEntity(em);
            em.persist(facility);
            em.flush();
        } else {
            facility = TestUtil.findAll(em, Facility.class).get(0);
        }
        room.setFacility(facility);
        return room;
    }

    @BeforeEach
    public void initTest() {
        room = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoom() throws Exception {
        int databaseSizeBeforeCreate = roomRepository.findAll().size();
        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);
        restRoomMockMvc.perform(post("/api/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isCreated());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeCreate + 1);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getRoomNumber()).isEqualTo(DEFAULT_ROOM_NUMBER);
        assertThat(testRoom.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createRoomWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roomRepository.findAll().size();

        // Create the Room with an existing ID
        room.setId(1L);
        RoomDTO roomDTO = roomMapper.toDto(room);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomMockMvc.perform(post("/api/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkRoomNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomRepository.findAll().size();
        // set the field null
        room.setRoomNumber(null);

        // Create the Room, which fails.
        RoomDTO roomDTO = roomMapper.toDto(room);


        restRoomMockMvc.perform(post("/api/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRooms() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList
        restRoomMockMvc.perform(get("/api/allRooms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(room.getId().intValue())))
            .andExpect(jsonPath("$.[*].roomNumber").value(hasItem(DEFAULT_ROOM_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get the room
        restRoomMockMvc.perform(get("/api/rooms/{id}", room.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(room.getId().intValue()))
            .andExpect(jsonPath("$.roomNumber").value(DEFAULT_ROOM_NUMBER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }


    @Test
    @Transactional
    public void getRoomsByIdFiltering() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        Long id = room.getId();

        defaultRoomShouldBeFound("id.equals=" + id);
        defaultRoomShouldNotBeFound("id.notEquals=" + id);

        defaultRoomShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRoomShouldNotBeFound("id.greaterThan=" + id);

        defaultRoomShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRoomShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRoomsByRoomNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where roomNumber equals to DEFAULT_ROOM_NUMBER
        defaultRoomShouldBeFound("roomNumber.equals=" + DEFAULT_ROOM_NUMBER);

        // Get all the roomList where roomNumber equals to UPDATED_ROOM_NUMBER
        defaultRoomShouldNotBeFound("roomNumber.equals=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    public void getAllRoomsByRoomNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where roomNumber not equals to DEFAULT_ROOM_NUMBER
        defaultRoomShouldNotBeFound("roomNumber.notEquals=" + DEFAULT_ROOM_NUMBER);

        // Get all the roomList where roomNumber not equals to UPDATED_ROOM_NUMBER
        defaultRoomShouldBeFound("roomNumber.notEquals=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    public void getAllRoomsByRoomNumberIsInShouldWork() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where roomNumber in DEFAULT_ROOM_NUMBER or UPDATED_ROOM_NUMBER
        defaultRoomShouldBeFound("roomNumber.in=" + DEFAULT_ROOM_NUMBER + "," + UPDATED_ROOM_NUMBER);

        // Get all the roomList where roomNumber equals to UPDATED_ROOM_NUMBER
        defaultRoomShouldNotBeFound("roomNumber.in=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    public void getAllRoomsByRoomNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where roomNumber is not null
        defaultRoomShouldBeFound("roomNumber.specified=true");

        // Get all the roomList where roomNumber is null
        defaultRoomShouldNotBeFound("roomNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllRoomsByRoomNumberContainsSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where roomNumber contains DEFAULT_ROOM_NUMBER
        defaultRoomShouldBeFound("roomNumber.contains=" + DEFAULT_ROOM_NUMBER);

        // Get all the roomList where roomNumber contains UPDATED_ROOM_NUMBER
        defaultRoomShouldNotBeFound("roomNumber.contains=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    public void getAllRoomsByRoomNumberNotContainsSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where roomNumber does not contain DEFAULT_ROOM_NUMBER
        defaultRoomShouldNotBeFound("roomNumber.doesNotContain=" + DEFAULT_ROOM_NUMBER);

        // Get all the roomList where roomNumber does not contain UPDATED_ROOM_NUMBER
        defaultRoomShouldBeFound("roomNumber.doesNotContain=" + UPDATED_ROOM_NUMBER);
    }


    @Test
    @Transactional
    public void getAllRoomsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where name equals to DEFAULT_NAME
        defaultRoomShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the roomList where name equals to UPDATED_NAME
        defaultRoomShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRoomsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where name not equals to DEFAULT_NAME
        defaultRoomShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the roomList where name not equals to UPDATED_NAME
        defaultRoomShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRoomsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRoomShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the roomList where name equals to UPDATED_NAME
        defaultRoomShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRoomsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where name is not null
        defaultRoomShouldBeFound("name.specified=true");

        // Get all the roomList where name is null
        defaultRoomShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllRoomsByNameContainsSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where name contains DEFAULT_NAME
        defaultRoomShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the roomList where name contains UPDATED_NAME
        defaultRoomShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRoomsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where name does not contain DEFAULT_NAME
        defaultRoomShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the roomList where name does not contain UPDATED_NAME
        defaultRoomShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }



    @Test
    @Transactional
    public void getAllRoomsByFacilityIsEqualToSomething() throws Exception {
        // Get already existing entity
        Facility facility = room.getFacility();
        roomRepository.saveAndFlush(room);
        Long facilityId = facility.getId();

        // Get all the roomList where facility equals to facilityId
        defaultRoomShouldBeFound("facilityId.equals=" + facilityId);

        // Get all the roomList where facility equals to facilityId + 1
        defaultRoomShouldNotBeFound("facilityId.equals=" + (facilityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRoomShouldBeFound(String filter) throws Exception {
        restRoomMockMvc.perform(get("/api/allRooms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(room.getId().intValue())))
            .andExpect(jsonPath("$.[*].roomNumber").value(hasItem(DEFAULT_ROOM_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restRoomMockMvc.perform(get("/api/rooms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRoomShouldNotBeFound(String filter) throws Exception {
        restRoomMockMvc.perform(get("/api/allRooms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRoomMockMvc.perform(get("/api/rooms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRoom() throws Exception {
        // Get the room
        restRoomMockMvc.perform(get("/api/rooms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        int databaseSizeBeforeUpdate = roomRepository.findAll().size();

        // Update the room
        Room updatedRoom = roomRepository.findById(room.getId()).get();
        // Disconnect from session so that the updates on updatedRoom are not directly saved in db
        em.detach(updatedRoom);
        updatedRoom
            .roomNumber(UPDATED_ROOM_NUMBER)
            .name(UPDATED_NAME);
        RoomDTO roomDTO = roomMapper.toDto(updatedRoom);

        restRoomMockMvc.perform(put("/api/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isOk());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getRoomNumber()).isEqualTo(UPDATED_ROOM_NUMBER);
        assertThat(testRoom.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomMockMvc.perform(put("/api/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        int databaseSizeBeforeDelete = roomRepository.findAll().size();

        // Delete the room
        restRoomMockMvc.perform(delete("/api/rooms/{id}", room.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
