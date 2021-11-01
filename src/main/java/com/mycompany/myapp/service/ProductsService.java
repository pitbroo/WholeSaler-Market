package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Products;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Products}.
 */
public interface ProductsService {
    /**
     * Save a products.
     *
     * @param products the entity to save.
     * @return the persisted entity.
     */
    Products save(Products products);

    /**
     * Partially updates a products.
     *
     * @param products the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Products> partialUpdate(Products products);

    /**
     * Get all the products.
     *
     * @return the list of entities.
     */
    List<Products> findAll();

    /**
     * Get the "id" products.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Products> findOne(Long id);

    /**
     * Delete the "id" products.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Products findByName(String name);
}
