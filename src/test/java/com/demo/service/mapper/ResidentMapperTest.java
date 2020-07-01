package com.demo.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ResidentMapperTest {

    private ResidentMapper residentMapper;

    @BeforeEach
    public void setUp() {
        residentMapper = new ResidentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(residentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(residentMapper.fromId(null)).isNull();
    }
}
