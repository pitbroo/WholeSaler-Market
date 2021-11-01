package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Products;
import com.mycompany.myapp.repository.ProductsRepository;
import com.mycompany.myapp.service.ProductsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Products}.
 */
@Service
@Transactional
public class ProductsServiceImpl implements ProductsService {

    private final Logger log = LoggerFactory.getLogger(ProductsServiceImpl.class);

    private final ProductsRepository productsRepository;

    public ProductsServiceImpl(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    public Products save(Products products) {
        log.debug("Request to save Products : {}", products);
        return productsRepository.save(products);
    }

    @Override
    public Optional<Products> partialUpdate(Products products) {
        log.debug("Request to partially update Products : {}", products);

        return productsRepository
            .findById(products.getId())
            .map(
                existingProducts -> {
                    if (products.getName() != null) {
                        existingProducts.setName(products.getName());
                    }
                    if (products.getPrice() != null) {
                        existingProducts.setPrice(products.getPrice());
                    }
                    if (products.getDescription() != null) {
                        existingProducts.setDescription(products.getDescription());
                    }

                    return existingProducts;
                }
            )
            .map(productsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Products> findAll() {
        log.debug("Request to get all Products");
        return productsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Products> findOne(Long id) {
        log.debug("Request to get Products : {}", id);
        return productsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Products : {}", id);
        productsRepository.deleteById(id);
    }

    @Override
    public Products findByName(String name) {
        return productsRepository.findProductsByName(name);
    }
}
