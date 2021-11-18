package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.FavouriteProduct;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link FavouriteProduct}.
 */
public interface FavouriteProductService {
    /**
     * Save a favouriteProduct.
     *
     * @param favouriteProduct the entity to save.
     * @return the persisted entity.
     */
    FavouriteProduct save(FavouriteProduct favouriteProduct);

    /**
     * Partially updates a favouriteProduct.
     *
     * @param favouriteProduct the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FavouriteProduct> partialUpdate(FavouriteProduct favouriteProduct);

    /**
     * Get all the favouriteProducts.
     *
     * @return the list of entities.
     */
    List<FavouriteProduct> findAll();


    @Transactional(readOnly = true)
    List<FavouriteProduct> findAllByUserId(Long userId);

    /**
     * Get the "id" favouriteProduct.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FavouriteProduct> findOne(Long id);

    /**
     * Delete the "id" favouriteProduct.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


}
