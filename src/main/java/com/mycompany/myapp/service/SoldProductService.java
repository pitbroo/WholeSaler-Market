package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SoldProduct;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link SoldProduct}.
 */
public interface SoldProductService {
    /**
     * Save a soldProduct.
     *
     * @param soldProduct the entity to save.
     * @return the persisted entity.
     */
    SoldProduct save(SoldProduct soldProduct);

    /**
     * Partially updates a soldProduct.
     *
     * @param soldProduct the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SoldProduct> partialUpdate(SoldProduct soldProduct);

    /**
     * Get all the soldProducts.
     *
     * @return the list of entities.
     */
    List<SoldProduct> findAll();

    /**
     * Get the "id" soldProduct.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SoldProduct> findOne(Long id);

    /**
     * Delete the "id" soldProduct.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
