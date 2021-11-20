package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.BoughtProduct;
import com.mycompany.myapp.repository.BoughtProductRepository;
import com.mycompany.myapp.service.BoughtProductService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BoughtProduct}.
 */
@Service
@Transactional
public class BoughtProductServiceImpl implements BoughtProductService {

    private final Logger log = LoggerFactory.getLogger(BoughtProductServiceImpl.class);

    private final BoughtProductRepository boughtProductRepository;

    public BoughtProductServiceImpl(BoughtProductRepository boughtProductRepository) {
        this.boughtProductRepository = boughtProductRepository;
    }

    @Override
    public BoughtProduct save(BoughtProduct boughtProduct) {
        log.debug("Request to save BoughtProduct : {}", boughtProduct);
        return boughtProductRepository.save(boughtProduct);
    }

    @Override
    public Optional<BoughtProduct> partialUpdate(BoughtProduct boughtProduct) {
        log.debug("Request to partially update BoughtProduct : {}", boughtProduct);

        return boughtProductRepository
            .findById(boughtProduct.getId())
            .map(existingBoughtProduct -> {
                if (boughtProduct.getDate() != null) {
                    existingBoughtProduct.setDate(boughtProduct.getDate());
                }
                if (boughtProduct.getPrice() != null) {
                    existingBoughtProduct.setPrice(boughtProduct.getPrice());
                }

                return existingBoughtProduct;
            })
            .map(boughtProductRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoughtProduct> findAll() {
        log.debug("Request to get all BoughtProducts");
        return boughtProductRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BoughtProduct> findOne(Long id) {
        log.debug("Request to get BoughtProduct : {}", id);
        return boughtProductRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BoughtProduct : {}", id);
        boughtProductRepository.deleteById(id);
    }
}
