package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BoughtProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoughtProduct.class);
        BoughtProduct boughtProduct1 = new BoughtProduct();
        boughtProduct1.setId(1L);
        BoughtProduct boughtProduct2 = new BoughtProduct();
        boughtProduct2.setId(boughtProduct1.getId());
        assertThat(boughtProduct1).isEqualTo(boughtProduct2);
        boughtProduct2.setId(2L);
        assertThat(boughtProduct1).isNotEqualTo(boughtProduct2);
        boughtProduct1.setId(null);
        assertThat(boughtProduct1).isNotEqualTo(boughtProduct2);
    }
}
