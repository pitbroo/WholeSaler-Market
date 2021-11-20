package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.BoughtProduct;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link BoughtProduct}.
 */
public interface BoughtProductService {
    /**
     * Save a boughtProduct.
     *
     * @param boughtProduct the entity to save.
     * @return the persisted entity.
     */
    BoughtProduct save(BoughtProduct boughtProduct);

    /**
     * Partially updates a boughtProduct.
     *
     * @param boughtProduct the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BoughtProduct> partialUpdate(BoughtProduct boughtProduct);

    /**
     * Get all the boughtProducts.
     *
     * @return the list of entities.
     */
    List<BoughtProduct> findAll();

    /**
     * Get the "id" boughtProduct.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BoughtProduct> findOne(Long id);

    /**
     * Delete the "id" boughtProduct.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
