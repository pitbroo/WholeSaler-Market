//package com.mycompany.myapp.web.rest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.mycompany.myapp.IntegrationTest;
//import com.mycompany.myapp.domain.Product;
//import com.mycompany.myapp.domain.SoldProduct;
//import com.mycompany.myapp.domain.User;
//import com.mycompany.myapp.repository.SoldProductRepository;
//import com.mycompany.myapp.service.criteria.SoldProductCriteria;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.atomic.AtomicLong;
//import javax.persistence.EntityManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Integration tests for the {@link SoldProductResource} REST controller.
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@WithMockUser
//class SoldProductResourceIT {
//
//    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
//    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
//    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);
//
//    private static final LocalDate DEFAULT_PRICE = LocalDate.ofEpochDay(0L);
//    private static final LocalDate UPDATED_PRICE = LocalDate.now(ZoneId.systemDefault());
//    private static final LocalDate SMALLER_PRICE = LocalDate.ofEpochDay(-1L);
//
//    private static final String ENTITY_API_URL = "/api/sold-products";
//    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
//
//    private static Random random = new Random();
//    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
//
//    @Autowired
//    private SoldProductRepository soldProductRepository;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private MockMvc restSoldProductMockMvc;
//
//    private SoldProduct soldProduct;
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static SoldProduct createEntity(EntityManager em) {
//        SoldProduct soldProduct = new SoldProduct().date(DEFAULT_DATE).price(DEFAULT_PRICE);
//        return soldProduct;
//    }
//
//    /**
//     * Create an updated entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static SoldProduct createUpdatedEntity(EntityManager em) {
//        SoldProduct soldProduct = new SoldProduct().date(UPDATED_DATE).price(UPDATED_PRICE);
//        return soldProduct;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        soldProduct = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    void createSoldProduct() throws Exception {
//        int databaseSizeBeforeCreate = soldProductRepository.findAll().size();
//        // Create the SoldProduct
//        restSoldProductMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soldProduct)))
//            .andExpect(status().isCreated());
//
//        // Validate the SoldProduct in the database
//        List<SoldProduct> soldProductList = soldProductRepository.findAll();
//        assertThat(soldProductList).hasSize(databaseSizeBeforeCreate + 1);
//        SoldProduct testSoldProduct = soldProductList.get(soldProductList.size() - 1);
//        assertThat(testSoldProduct.getDate()).isEqualTo(DEFAULT_DATE);
//        assertThat(testSoldProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
//    }
//
//    @Test
//    @Transactional
//    void createSoldProductWithExistingId() throws Exception {
//        // Create the SoldProduct with an existing ID
//        soldProduct.setId(1L);
//
//        int databaseSizeBeforeCreate = soldProductRepository.findAll().size();
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restSoldProductMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soldProduct)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the SoldProduct in the database
//        List<SoldProduct> soldProductList = soldProductRepository.findAll();
//        assertThat(soldProductList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProducts() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList
//        restSoldProductMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(soldProduct.getId().intValue())))
//            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
//            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.toString())));
//    }
//
//    @Test
//    @Transactional
//    void getSoldProduct() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get the soldProduct
//        restSoldProductMockMvc
//            .perform(get(ENTITY_API_URL_ID, soldProduct.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(soldProduct.getId().intValue()))
//            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
//            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.toString()));
//    }
//
//    @Test
//    @Transactional
//    void getSoldProductsByIdFiltering() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        Long id = soldProduct.getId();
//
//        defaultSoldProductShouldBeFound("id.equals=" + id);
//        defaultSoldProductShouldNotBeFound("id.notEquals=" + id);
//
//        defaultSoldProductShouldBeFound("id.greaterThanOrEqual=" + id);
//        defaultSoldProductShouldNotBeFound("id.greaterThan=" + id);
//
//        defaultSoldProductShouldBeFound("id.lessThanOrEqual=" + id);
//        defaultSoldProductShouldNotBeFound("id.lessThan=" + id);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByDateIsEqualToSomething() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList where date equals to DEFAULT_DATE
//        defaultSoldProductShouldBeFound("date.equals=" + DEFAULT_DATE);
//
//        // Get all the soldProductList where date equals to UPDATED_DATE
//        defaultSoldProductShouldNotBeFound("date.equals=" + UPDATED_DATE);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByDateIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList where date not equals to DEFAULT_DATE
//        defaultSoldProductShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);
//
//        // Get all the soldProductList where date not equals to UPDATED_DATE
//        defaultSoldProductShouldBeFound("date.notEquals=" + UPDATED_DATE);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByDateIsInShouldWork() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList where date in DEFAULT_DATE or UPDATED_DATE
//        defaultSoldProductShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);
//
//        // Get all the soldProductList where date equals to UPDATED_DATE
//        defaultSoldProductShouldNotBeFound("date.in=" + UPDATED_DATE);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByDateIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList where date is not null
//        defaultSoldProductShouldBeFound("date.specified=true");
//
//        // Get all the soldProductList where date is null
//        defaultSoldProductShouldNotBeFound("date.specified=false");
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByDateIsGreaterThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList where date is greater than or equal to DEFAULT_DATE
//        defaultSoldProductShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);
//
//        // Get all the soldProductList where date is greater than or equal to UPDATED_DATE
//        defaultSoldProductShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByDateIsLessThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList where date is less than or equal to DEFAULT_DATE
//        defaultSoldProductShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);
//
//        // Get all the soldProductList where date is less than or equal to SMALLER_DATE
//        defaultSoldProductShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByDateIsLessThanSomething() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList where date is less than DEFAULT_DATE
//        defaultSoldProductShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);
//
//        // Get all the soldProductList where date is less than UPDATED_DATE
//        defaultSoldProductShouldBeFound("date.lessThan=" + UPDATED_DATE);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByDateIsGreaterThanSomething() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList where date is greater than DEFAULT_DATE
//        defaultSoldProductShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);
//
//        // Get all the soldProductList where date is greater than SMALLER_DATE
//        defaultSoldProductShouldBeFound("date.greaterThan=" + SMALLER_DATE);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByPriceIsEqualToSomething() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList where price equals to DEFAULT_PRICE
//        defaultSoldProductShouldBeFound("price.equals=" + DEFAULT_PRICE);
//
//        // Get all the soldProductList where price equals to UPDATED_PRICE
//        defaultSoldProductShouldNotBeFound("price.equals=" + UPDATED_PRICE);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByPriceIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList where price not equals to DEFAULT_PRICE
//        defaultSoldProductShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);
//
//        // Get all the soldProductList where price not equals to UPDATED_PRICE
//        defaultSoldProductShouldBeFound("price.notEquals=" + UPDATED_PRICE);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByPriceIsInShouldWork() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList where price in DEFAULT_PRICE or UPDATED_PRICE
//        defaultSoldProductShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);
//
//        // Get all the soldProductList where price equals to UPDATED_PRICE
//        defaultSoldProductShouldNotBeFound("price.in=" + UPDATED_PRICE);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByPriceIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList where price is not null
//        defaultSoldProductShouldBeFound("price.specified=true");
//
//        // Get all the soldProductList where price is null
//        defaultSoldProductShouldNotBeFound("price.specified=false");
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList where price is greater than or equal to DEFAULT_PRICE
//        defaultSoldProductShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);
//
//        // Get all the soldProductList where price is greater than or equal to UPDATED_PRICE
//        defaultSoldProductShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByPriceIsLessThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList where price is less than or equal to DEFAULT_PRICE
//        defaultSoldProductShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);
//
//        // Get all the soldProductList where price is less than or equal to SMALLER_PRICE
//        defaultSoldProductShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByPriceIsLessThanSomething() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList where price is less than DEFAULT_PRICE
//        defaultSoldProductShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);
//
//        // Get all the soldProductList where price is less than UPDATED_PRICE
//        defaultSoldProductShouldBeFound("price.lessThan=" + UPDATED_PRICE);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByPriceIsGreaterThanSomething() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        // Get all the soldProductList where price is greater than DEFAULT_PRICE
//        defaultSoldProductShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);
//
//        // Get all the soldProductList where price is greater than SMALLER_PRICE
//        defaultSoldProductShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByUserIsEqualToSomething() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//        User user;
//        if (TestUtil.findAll(em, User.class).isEmpty()) {
//            user = UserResourceIT.createEntity(em);
//            em.persist(user);
//            em.flush();
//        } else {
//            user = TestUtil.findAll(em, User.class).get(0);
//        }
//        em.persist(user);
//        em.flush();
//        soldProduct.setUser(user);
//        soldProductRepository.saveAndFlush(soldProduct);
//        Long userId = user.getId();
//
//        // Get all the soldProductList where user equals to userId
//        defaultSoldProductShouldBeFound("userId.equals=" + userId);
//
//        // Get all the soldProductList where user equals to (userId + 1)
//        defaultSoldProductShouldNotBeFound("userId.equals=" + (userId + 1));
//    }
//
//    @Test
//    @Transactional
//    void getAllSoldProductsByProductIsEqualToSomething() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//        Product product;
//        if (TestUtil.findAll(em, Product.class).isEmpty()) {
//            product = ProductResourceIT.createEntity(em);
//            em.persist(product);
//            em.flush();
//        } else {
//            product = TestUtil.findAll(em, Product.class).get(0);
//        }
//        em.persist(product);
//        em.flush();
//        soldProduct.setProduct(product);
//        soldProductRepository.saveAndFlush(soldProduct);
//        Long productId = product.getId();
//
//        // Get all the soldProductList where product equals to productId
//        defaultSoldProductShouldBeFound("productId.equals=" + productId);
//
//        // Get all the soldProductList where product equals to (productId + 1)
//        defaultSoldProductShouldNotBeFound("productId.equals=" + (productId + 1));
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is returned.
//     */
//    private void defaultSoldProductShouldBeFound(String filter) throws Exception {
//        restSoldProductMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(soldProduct.getId().intValue())))
//            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
//            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.toString())));
//
//        // Check, that the count call also returns 1
//        restSoldProductMockMvc
//            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(content().string("1"));
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is not returned.
//     */
//    private void defaultSoldProductShouldNotBeFound(String filter) throws Exception {
//        restSoldProductMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$").isArray())
//            .andExpect(jsonPath("$").isEmpty());
//
//        // Check, that the count call also returns 0
//        restSoldProductMockMvc
//            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(content().string("0"));
//    }
//
//    @Test
//    @Transactional
//    void getNonExistingSoldProduct() throws Exception {
//        // Get the soldProduct
//        restSoldProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    void putNewSoldProduct() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        int databaseSizeBeforeUpdate = soldProductRepository.findAll().size();
//
//        // Update the soldProduct
//        SoldProduct updatedSoldProduct = soldProductRepository.findById(soldProduct.getId()).get();
//        // Disconnect from session so that the updates on updatedSoldProduct are not directly saved in db
//        em.detach(updatedSoldProduct);
//        updatedSoldProduct.date(UPDATED_DATE).price(UPDATED_PRICE);
//
//        restSoldProductMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, updatedSoldProduct.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(updatedSoldProduct))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the SoldProduct in the database
//        List<SoldProduct> soldProductList = soldProductRepository.findAll();
//        assertThat(soldProductList).hasSize(databaseSizeBeforeUpdate);
//        SoldProduct testSoldProduct = soldProductList.get(soldProductList.size() - 1);
//        assertThat(testSoldProduct.getDate()).isEqualTo(UPDATED_DATE);
//        assertThat(testSoldProduct.getPrice()).isEqualTo(UPDATED_PRICE);
//    }
//
//    @Test
//    @Transactional
//    void putNonExistingSoldProduct() throws Exception {
//        int databaseSizeBeforeUpdate = soldProductRepository.findAll().size();
//        soldProduct.setId(count.incrementAndGet());
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restSoldProductMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, soldProduct.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(soldProduct))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the SoldProduct in the database
//        List<SoldProduct> soldProductList = soldProductRepository.findAll();
//        assertThat(soldProductList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithIdMismatchSoldProduct() throws Exception {
//        int databaseSizeBeforeUpdate = soldProductRepository.findAll().size();
//        soldProduct.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restSoldProductMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(soldProduct))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the SoldProduct in the database
//        List<SoldProduct> soldProductList = soldProductRepository.findAll();
//        assertThat(soldProductList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithMissingIdPathParamSoldProduct() throws Exception {
//        int databaseSizeBeforeUpdate = soldProductRepository.findAll().size();
//        soldProduct.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restSoldProductMockMvc
//            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soldProduct)))
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the SoldProduct in the database
//        List<SoldProduct> soldProductList = soldProductRepository.findAll();
//        assertThat(soldProductList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void partialUpdateSoldProductWithPatch() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        int databaseSizeBeforeUpdate = soldProductRepository.findAll().size();
//
//        // Update the soldProduct using partial update
//        SoldProduct partialUpdatedSoldProduct = new SoldProduct();
//        partialUpdatedSoldProduct.setId(soldProduct.getId());
//
//        partialUpdatedSoldProduct.price(UPDATED_PRICE);
//
//        restSoldProductMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedSoldProduct.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSoldProduct))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the SoldProduct in the database
//        List<SoldProduct> soldProductList = soldProductRepository.findAll();
//        assertThat(soldProductList).hasSize(databaseSizeBeforeUpdate);
//        SoldProduct testSoldProduct = soldProductList.get(soldProductList.size() - 1);
//        assertThat(testSoldProduct.getDate()).isEqualTo(DEFAULT_DATE);
//        assertThat(testSoldProduct.getPrice()).isEqualTo(UPDATED_PRICE);
//    }
//
//    @Test
//    @Transactional
//    void fullUpdateSoldProductWithPatch() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        int databaseSizeBeforeUpdate = soldProductRepository.findAll().size();
//
//        // Update the soldProduct using partial update
//        SoldProduct partialUpdatedSoldProduct = new SoldProduct();
//        partialUpdatedSoldProduct.setId(soldProduct.getId());
//
//        partialUpdatedSoldProduct.date(UPDATED_DATE).price(UPDATED_PRICE);
//
//        restSoldProductMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedSoldProduct.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSoldProduct))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the SoldProduct in the database
//        List<SoldProduct> soldProductList = soldProductRepository.findAll();
//        assertThat(soldProductList).hasSize(databaseSizeBeforeUpdate);
//        SoldProduct testSoldProduct = soldProductList.get(soldProductList.size() - 1);
//        assertThat(testSoldProduct.getDate()).isEqualTo(UPDATED_DATE);
//        assertThat(testSoldProduct.getPrice()).isEqualTo(UPDATED_PRICE);
//    }
//
//    @Test
//    @Transactional
//    void patchNonExistingSoldProduct() throws Exception {
//        int databaseSizeBeforeUpdate = soldProductRepository.findAll().size();
//        soldProduct.setId(count.incrementAndGet());
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restSoldProductMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, soldProduct.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(soldProduct))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the SoldProduct in the database
//        List<SoldProduct> soldProductList = soldProductRepository.findAll();
//        assertThat(soldProductList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithIdMismatchSoldProduct() throws Exception {
//        int databaseSizeBeforeUpdate = soldProductRepository.findAll().size();
//        soldProduct.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restSoldProductMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(soldProduct))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the SoldProduct in the database
//        List<SoldProduct> soldProductList = soldProductRepository.findAll();
//        assertThat(soldProductList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithMissingIdPathParamSoldProduct() throws Exception {
//        int databaseSizeBeforeUpdate = soldProductRepository.findAll().size();
//        soldProduct.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restSoldProductMockMvc
//            .perform(
//                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(soldProduct))
//            )
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the SoldProduct in the database
//        List<SoldProduct> soldProductList = soldProductRepository.findAll();
//        assertThat(soldProductList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void deleteSoldProduct() throws Exception {
//        // Initialize the database
//        soldProductRepository.saveAndFlush(soldProduct);
//
//        int databaseSizeBeforeDelete = soldProductRepository.findAll().size();
//
//        // Delete the soldProduct
//        restSoldProductMockMvc
//            .perform(delete(ENTITY_API_URL_ID, soldProduct.getId()).accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isNoContent());
//
//        // Validate the database contains one less item
//        List<SoldProduct> soldProductList = soldProductRepository.findAll();
//        assertThat(soldProductList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}
