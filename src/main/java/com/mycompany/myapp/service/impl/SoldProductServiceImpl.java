package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.SoldProduct;
import com.mycompany.myapp.repository.SoldProductRepository;
import com.mycompany.myapp.service.SoldProductService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SoldProduct}.
 */
@Service
@Transactional
public class SoldProductServiceImpl implements SoldProductService {

    private final Logger log = LoggerFactory.getLogger(SoldProductServiceImpl.class);

    private final SoldProductRepository soldProductRepository;

    public SoldProductServiceImpl(SoldProductRepository soldProductRepository) {
        this.soldProductRepository = soldProductRepository;
    }

    @Override
    public SoldProduct save(SoldProduct soldProduct) {
        log.debug("Request to save SoldProduct : {}", soldProduct);
        return soldProductRepository.save(soldProduct);
    }

    @Override
    public Optional<SoldProduct> partialUpdate(SoldProduct soldProduct) {
        log.debug("Request to partially update SoldProduct : {}", soldProduct);

        return soldProductRepository
            .findById(soldProduct.getId())
            .map(existingSoldProduct -> {
                if (soldProduct.getDate() != null) {
                    existingSoldProduct.setDate(soldProduct.getDate());
                }
                if (soldProduct.getPrice() != null) {
                    existingSoldProduct.setPrice(soldProduct.getPrice());
                }

                return existingSoldProduct;
            })
            .map(soldProductRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SoldProduct> findAll() {
        log.debug("Request to get all SoldProducts");
        return soldProductRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SoldProduct> findOne(Long id) {
        log.debug("Request to get SoldProduct : {}", id);
        return soldProductRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SoldProduct : {}", id);
        soldProductRepository.deleteById(id);
    }
}
