package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BoughtProduct;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.BoughtProductRepository;
import com.mycompany.myapp.service.criteria.BoughtProductCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BoughtProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BoughtProductResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_PRICE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PRICE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PRICE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/bought-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BoughtProductRepository boughtProductRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBoughtProductMockMvc;

    private BoughtProduct boughtProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoughtProduct createEntity(EntityManager em) {
        BoughtProduct boughtProduct = new BoughtProduct().date(DEFAULT_DATE).price(DEFAULT_PRICE);
        return boughtProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoughtProduct createUpdatedEntity(EntityManager em) {
        BoughtProduct boughtProduct = new BoughtProduct().date(UPDATED_DATE).price(UPDATED_PRICE);
        return boughtProduct;
    }

    @BeforeEach
    public void initTest() {
        boughtProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createBoughtProduct() throws Exception {
        int databaseSizeBeforeCreate = boughtProductRepository.findAll().size();
        // Create the BoughtProduct
        restBoughtProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boughtProduct)))
            .andExpect(status().isCreated());

        // Validate the BoughtProduct in the database
        List<BoughtProduct> boughtProductList = boughtProductRepository.findAll();
        assertThat(boughtProductList).hasSize(databaseSizeBeforeCreate + 1);
        BoughtProduct testBoughtProduct = boughtProductList.get(boughtProductList.size() - 1);
        assertThat(testBoughtProduct.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBoughtProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createBoughtProductWithExistingId() throws Exception {
        // Create the BoughtProduct with an existing ID
        boughtProduct.setId(1L);

        int databaseSizeBeforeCreate = boughtProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoughtProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boughtProduct)))
            .andExpect(status().isBadRequest());

        // Validate the BoughtProduct in the database
        List<BoughtProduct> boughtProductList = boughtProductRepository.findAll();
        assertThat(boughtProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBoughtProducts() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList
        restBoughtProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boughtProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.toString())));
    }

    @Test
    @Transactional
    void getBoughtProduct() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get the boughtProduct
        restBoughtProductMockMvc
            .perform(get(ENTITY_API_URL_ID, boughtProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(boughtProduct.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.toString()));
    }

    @Test
    @Transactional
    void getBoughtProductsByIdFiltering() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        Long id = boughtProduct.getId();

        defaultBoughtProductShouldBeFound("id.equals=" + id);
        defaultBoughtProductShouldNotBeFound("id.notEquals=" + id);

        defaultBoughtProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBoughtProductShouldNotBeFound("id.greaterThan=" + id);

        defaultBoughtProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBoughtProductShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBoughtProductsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList where date equals to DEFAULT_DATE
        defaultBoughtProductShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the boughtProductList where date equals to UPDATED_DATE
        defaultBoughtProductShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBoughtProductsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList where date not equals to DEFAULT_DATE
        defaultBoughtProductShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the boughtProductList where date not equals to UPDATED_DATE
        defaultBoughtProductShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBoughtProductsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList where date in DEFAULT_DATE or UPDATED_DATE
        defaultBoughtProductShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the boughtProductList where date equals to UPDATED_DATE
        defaultBoughtProductShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBoughtProductsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList where date is not null
        defaultBoughtProductShouldBeFound("date.specified=true");

        // Get all the boughtProductList where date is null
        defaultBoughtProductShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllBoughtProductsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList where date is greater than or equal to DEFAULT_DATE
        defaultBoughtProductShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the boughtProductList where date is greater than or equal to UPDATED_DATE
        defaultBoughtProductShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBoughtProductsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList where date is less than or equal to DEFAULT_DATE
        defaultBoughtProductShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the boughtProductList where date is less than or equal to SMALLER_DATE
        defaultBoughtProductShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllBoughtProductsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList where date is less than DEFAULT_DATE
        defaultBoughtProductShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the boughtProductList where date is less than UPDATED_DATE
        defaultBoughtProductShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBoughtProductsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList where date is greater than DEFAULT_DATE
        defaultBoughtProductShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the boughtProductList where date is greater than SMALLER_DATE
        defaultBoughtProductShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllBoughtProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList where price equals to DEFAULT_PRICE
        defaultBoughtProductShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the boughtProductList where price equals to UPDATED_PRICE
        defaultBoughtProductShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBoughtProductsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList where price not equals to DEFAULT_PRICE
        defaultBoughtProductShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the boughtProductList where price not equals to UPDATED_PRICE
        defaultBoughtProductShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBoughtProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultBoughtProductShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the boughtProductList where price equals to UPDATED_PRICE
        defaultBoughtProductShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBoughtProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList where price is not null
        defaultBoughtProductShouldBeFound("price.specified=true");

        // Get all the boughtProductList where price is null
        defaultBoughtProductShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllBoughtProductsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList where price is greater than or equal to DEFAULT_PRICE
        defaultBoughtProductShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the boughtProductList where price is greater than or equal to UPDATED_PRICE
        defaultBoughtProductShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBoughtProductsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList where price is less than or equal to DEFAULT_PRICE
        defaultBoughtProductShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the boughtProductList where price is less than or equal to SMALLER_PRICE
        defaultBoughtProductShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllBoughtProductsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList where price is less than DEFAULT_PRICE
        defaultBoughtProductShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the boughtProductList where price is less than UPDATED_PRICE
        defaultBoughtProductShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBoughtProductsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        // Get all the boughtProductList where price is greater than DEFAULT_PRICE
        defaultBoughtProductShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the boughtProductList where price is greater than SMALLER_PRICE
        defaultBoughtProductShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllBoughtProductsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            user = UserResourceIT.createEntity(em);
            em.persist(user);
            em.flush();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        boughtProduct.setUser(user);
        boughtProductRepository.saveAndFlush(boughtProduct);
        Long userId = user.getId();

        // Get all the boughtProductList where user equals to userId
        defaultBoughtProductShouldBeFound("userId.equals=" + userId);

        // Get all the boughtProductList where user equals to (userId + 1)
        defaultBoughtProductShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllBoughtProductsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        boughtProduct.setProduct(product);
        boughtProductRepository.saveAndFlush(boughtProduct);
        Long productId = product.getId();

        // Get all the boughtProductList where product equals to productId
        defaultBoughtProductShouldBeFound("productId.equals=" + productId);

        // Get all the boughtProductList where product equals to (productId + 1)
        defaultBoughtProductShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBoughtProductShouldBeFound(String filter) throws Exception {
        restBoughtProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boughtProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.toString())));

        // Check, that the count call also returns 1
        restBoughtProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBoughtProductShouldNotBeFound(String filter) throws Exception {
        restBoughtProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBoughtProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBoughtProduct() throws Exception {
        // Get the boughtProduct
        restBoughtProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBoughtProduct() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        int databaseSizeBeforeUpdate = boughtProductRepository.findAll().size();

        // Update the boughtProduct
        BoughtProduct updatedBoughtProduct = boughtProductRepository.findById(boughtProduct.getId()).get();
        // Disconnect from session so that the updates on updatedBoughtProduct are not directly saved in db
        em.detach(updatedBoughtProduct);
        updatedBoughtProduct.date(UPDATED_DATE).price(UPDATED_PRICE);

        restBoughtProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBoughtProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBoughtProduct))
            )
            .andExpect(status().isOk());

        // Validate the BoughtProduct in the database
        List<BoughtProduct> boughtProductList = boughtProductRepository.findAll();
        assertThat(boughtProductList).hasSize(databaseSizeBeforeUpdate);
        BoughtProduct testBoughtProduct = boughtProductList.get(boughtProductList.size() - 1);
        assertThat(testBoughtProduct.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBoughtProduct.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingBoughtProduct() throws Exception {
        int databaseSizeBeforeUpdate = boughtProductRepository.findAll().size();
        boughtProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoughtProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, boughtProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boughtProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoughtProduct in the database
        List<BoughtProduct> boughtProductList = boughtProductRepository.findAll();
        assertThat(boughtProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBoughtProduct() throws Exception {
        int databaseSizeBeforeUpdate = boughtProductRepository.findAll().size();
        boughtProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoughtProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boughtProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoughtProduct in the database
        List<BoughtProduct> boughtProductList = boughtProductRepository.findAll();
        assertThat(boughtProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBoughtProduct() throws Exception {
        int databaseSizeBeforeUpdate = boughtProductRepository.findAll().size();
        boughtProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoughtProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boughtProduct)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BoughtProduct in the database
        List<BoughtProduct> boughtProductList = boughtProductRepository.findAll();
        assertThat(boughtProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBoughtProductWithPatch() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        int databaseSizeBeforeUpdate = boughtProductRepository.findAll().size();

        // Update the boughtProduct using partial update
        BoughtProduct partialUpdatedBoughtProduct = new BoughtProduct();
        partialUpdatedBoughtProduct.setId(boughtProduct.getId());

        restBoughtProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoughtProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoughtProduct))
            )
            .andExpect(status().isOk());

        // Validate the BoughtProduct in the database
        List<BoughtProduct> boughtProductList = boughtProductRepository.findAll();
        assertThat(boughtProductList).hasSize(databaseSizeBeforeUpdate);
        BoughtProduct testBoughtProduct = boughtProductList.get(boughtProductList.size() - 1);
        assertThat(testBoughtProduct.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBoughtProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateBoughtProductWithPatch() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        int databaseSizeBeforeUpdate = boughtProductRepository.findAll().size();

        // Update the boughtProduct using partial update
        BoughtProduct partialUpdatedBoughtProduct = new BoughtProduct();
        partialUpdatedBoughtProduct.setId(boughtProduct.getId());

        partialUpdatedBoughtProduct.date(UPDATED_DATE).price(UPDATED_PRICE);

        restBoughtProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoughtProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoughtProduct))
            )
            .andExpect(status().isOk());

        // Validate the BoughtProduct in the database
        List<BoughtProduct> boughtProductList = boughtProductRepository.findAll();
        assertThat(boughtProductList).hasSize(databaseSizeBeforeUpdate);
        BoughtProduct testBoughtProduct = boughtProductList.get(boughtProductList.size() - 1);
        assertThat(testBoughtProduct.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBoughtProduct.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingBoughtProduct() throws Exception {
        int databaseSizeBeforeUpdate = boughtProductRepository.findAll().size();
        boughtProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoughtProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, boughtProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boughtProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoughtProduct in the database
        List<BoughtProduct> boughtProductList = boughtProductRepository.findAll();
        assertThat(boughtProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBoughtProduct() throws Exception {
        int databaseSizeBeforeUpdate = boughtProductRepository.findAll().size();
        boughtProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoughtProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boughtProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoughtProduct in the database
        List<BoughtProduct> boughtProductList = boughtProductRepository.findAll();
        assertThat(boughtProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBoughtProduct() throws Exception {
        int databaseSizeBeforeUpdate = boughtProductRepository.findAll().size();
        boughtProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoughtProductMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(boughtProduct))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BoughtProduct in the database
        List<BoughtProduct> boughtProductList = boughtProductRepository.findAll();
        assertThat(boughtProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBoughtProduct() throws Exception {
        // Initialize the database
        boughtProductRepository.saveAndFlush(boughtProduct);

        int databaseSizeBeforeDelete = boughtProductRepository.findAll().size();

        // Delete the boughtProduct
        restBoughtProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, boughtProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BoughtProduct> boughtProductList = boughtProductRepository.findAll();
        assertThat(boughtProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
