package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.FavouriteProduct;
import com.mycompany.myapp.repository.FavouriteProductRepository;
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
 * Integration tests for the {@link FavouriteProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FavouriteProductResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/favourite-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FavouriteProductRepository favouriteProductRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFavouriteProductMockMvc;

    private FavouriteProduct favouriteProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FavouriteProduct createEntity(EntityManager em) {
        FavouriteProduct favouriteProduct = new FavouriteProduct().userId(DEFAULT_USER_ID);
        return favouriteProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FavouriteProduct createUpdatedEntity(EntityManager em) {
        FavouriteProduct favouriteProduct = new FavouriteProduct().userId(UPDATED_USER_ID);
        return favouriteProduct;
    }

    @BeforeEach
    public void initTest() {
        favouriteProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createFavouriteProduct() throws Exception {
        int databaseSizeBeforeCreate = favouriteProductRepository.findAll().size();
        // Create the FavouriteProduct
        restFavouriteProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(favouriteProduct))
            )
            .andExpect(status().isCreated());

        // Validate the FavouriteProduct in the database
        List<FavouriteProduct> favouriteProductList = favouriteProductRepository.findAll();
        assertThat(favouriteProductList).hasSize(databaseSizeBeforeCreate + 1);
        FavouriteProduct testFavouriteProduct = favouriteProductList.get(favouriteProductList.size() - 1);
        assertThat(testFavouriteProduct.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void createFavouriteProductWithExistingId() throws Exception {
        // Create the FavouriteProduct with an existing ID
        favouriteProduct.setId(1L);

        int databaseSizeBeforeCreate = favouriteProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFavouriteProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(favouriteProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the FavouriteProduct in the database
        List<FavouriteProduct> favouriteProductList = favouriteProductRepository.findAll();
        assertThat(favouriteProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFavouriteProducts() throws Exception {
        // Initialize the database
        favouriteProductRepository.saveAndFlush(favouriteProduct);

        // Get all the favouriteProductList
        restFavouriteProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favouriteProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    void getFavouriteProduct() throws Exception {
        // Initialize the database
        favouriteProductRepository.saveAndFlush(favouriteProduct);

        // Get the favouriteProduct
        restFavouriteProductMockMvc
            .perform(get(ENTITY_API_URL_ID, favouriteProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(favouriteProduct.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingFavouriteProduct() throws Exception {
        // Get the favouriteProduct
        restFavouriteProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFavouriteProduct() throws Exception {
        // Initialize the database
        favouriteProductRepository.saveAndFlush(favouriteProduct);

        int databaseSizeBeforeUpdate = favouriteProductRepository.findAll().size();

        // Update the favouriteProduct
        FavouriteProduct updatedFavouriteProduct = favouriteProductRepository.findById(favouriteProduct.getId()).get();
        // Disconnect from session so that the updates on updatedFavouriteProduct are not directly saved in db
        em.detach(updatedFavouriteProduct);
        updatedFavouriteProduct.userId(UPDATED_USER_ID);

        restFavouriteProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFavouriteProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFavouriteProduct))
            )
            .andExpect(status().isOk());

        // Validate the FavouriteProduct in the database
        List<FavouriteProduct> favouriteProductList = favouriteProductRepository.findAll();
        assertThat(favouriteProductList).hasSize(databaseSizeBeforeUpdate);
        FavouriteProduct testFavouriteProduct = favouriteProductList.get(favouriteProductList.size() - 1);
        assertThat(testFavouriteProduct.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingFavouriteProduct() throws Exception {
        int databaseSizeBeforeUpdate = favouriteProductRepository.findAll().size();
        favouriteProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavouriteProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, favouriteProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(favouriteProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the FavouriteProduct in the database
        List<FavouriteProduct> favouriteProductList = favouriteProductRepository.findAll();
        assertThat(favouriteProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFavouriteProduct() throws Exception {
        int databaseSizeBeforeUpdate = favouriteProductRepository.findAll().size();
        favouriteProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavouriteProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(favouriteProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the FavouriteProduct in the database
        List<FavouriteProduct> favouriteProductList = favouriteProductRepository.findAll();
        assertThat(favouriteProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFavouriteProduct() throws Exception {
        int databaseSizeBeforeUpdate = favouriteProductRepository.findAll().size();
        favouriteProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavouriteProductMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(favouriteProduct))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FavouriteProduct in the database
        List<FavouriteProduct> favouriteProductList = favouriteProductRepository.findAll();
        assertThat(favouriteProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFavouriteProductWithPatch() throws Exception {
        // Initialize the database
        favouriteProductRepository.saveAndFlush(favouriteProduct);

        int databaseSizeBeforeUpdate = favouriteProductRepository.findAll().size();

        // Update the favouriteProduct using partial update
        FavouriteProduct partialUpdatedFavouriteProduct = new FavouriteProduct();
        partialUpdatedFavouriteProduct.setId(favouriteProduct.getId());

        restFavouriteProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFavouriteProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFavouriteProduct))
            )
            .andExpect(status().isOk());

        // Validate the FavouriteProduct in the database
        List<FavouriteProduct> favouriteProductList = favouriteProductRepository.findAll();
        assertThat(favouriteProductList).hasSize(databaseSizeBeforeUpdate);
        FavouriteProduct testFavouriteProduct = favouriteProductList.get(favouriteProductList.size() - 1);
        assertThat(testFavouriteProduct.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateFavouriteProductWithPatch() throws Exception {
        // Initialize the database
        favouriteProductRepository.saveAndFlush(favouriteProduct);

        int databaseSizeBeforeUpdate = favouriteProductRepository.findAll().size();

        // Update the favouriteProduct using partial update
        FavouriteProduct partialUpdatedFavouriteProduct = new FavouriteProduct();
        partialUpdatedFavouriteProduct.setId(favouriteProduct.getId());

        partialUpdatedFavouriteProduct.userId(UPDATED_USER_ID);

        restFavouriteProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFavouriteProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFavouriteProduct))
            )
            .andExpect(status().isOk());

        // Validate the FavouriteProduct in the database
        List<FavouriteProduct> favouriteProductList = favouriteProductRepository.findAll();
        assertThat(favouriteProductList).hasSize(databaseSizeBeforeUpdate);
        FavouriteProduct testFavouriteProduct = favouriteProductList.get(favouriteProductList.size() - 1);
        assertThat(testFavouriteProduct.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingFavouriteProduct() throws Exception {
        int databaseSizeBeforeUpdate = favouriteProductRepository.findAll().size();
        favouriteProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavouriteProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, favouriteProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(favouriteProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the FavouriteProduct in the database
        List<FavouriteProduct> favouriteProductList = favouriteProductRepository.findAll();
        assertThat(favouriteProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFavouriteProduct() throws Exception {
        int databaseSizeBeforeUpdate = favouriteProductRepository.findAll().size();
        favouriteProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavouriteProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(favouriteProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the FavouriteProduct in the database
        List<FavouriteProduct> favouriteProductList = favouriteProductRepository.findAll();
        assertThat(favouriteProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFavouriteProduct() throws Exception {
        int databaseSizeBeforeUpdate = favouriteProductRepository.findAll().size();
        favouriteProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavouriteProductMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(favouriteProduct))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FavouriteProduct in the database
        List<FavouriteProduct> favouriteProductList = favouriteProductRepository.findAll();
        assertThat(favouriteProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFavouriteProduct() throws Exception {
        // Initialize the database
        favouriteProductRepository.saveAndFlush(favouriteProduct);

        int databaseSizeBeforeDelete = favouriteProductRepository.findAll().size();

        // Delete the favouriteProduct
        restFavouriteProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, favouriteProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FavouriteProduct> favouriteProductList = favouriteProductRepository.findAll();
        assertThat(favouriteProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
