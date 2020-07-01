package com.demo.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.demo.web.rest.TestUtil;

public class ResidentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resident.class);
        Resident resident1 = new Resident();
        resident1.setId(1L);
        Resident resident2 = new Resident();
        resident2.setId(resident1.getId());
        assertThat(resident1).isEqualTo(resident2);
        resident2.setId(2L);
        assertThat(resident1).isNotEqualTo(resident2);
        resident1.setId(null);
        assertThat(resident1).isNotEqualTo(resident2);
    }
}
