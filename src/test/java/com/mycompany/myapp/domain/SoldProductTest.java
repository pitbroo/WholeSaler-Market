package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SoldProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SoldProduct.class);
        SoldProduct soldProduct1 = new SoldProduct();
        soldProduct1.setId(1L);
        SoldProduct soldProduct2 = new SoldProduct();
        soldProduct2.setId(soldProduct1.getId());
        assertThat(soldProduct1).isEqualTo(soldProduct2);
        soldProduct2.setId(2L);
        assertThat(soldProduct1).isNotEqualTo(soldProduct2);
        soldProduct1.setId(null);
        assertThat(soldProduct1).isNotEqualTo(soldProduct2);
    }
}
