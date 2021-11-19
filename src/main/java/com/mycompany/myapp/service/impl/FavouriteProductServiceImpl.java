package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.FavouriteProduct;
import com.mycompany.myapp.repository.FavouriteProductRepository;
import com.mycompany.myapp.service.FavouriteProductService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FavouriteProduct}.
 */
@Service
@Transactional
public class FavouriteProductServiceImpl implements FavouriteProductService {

    private final Logger log = LoggerFactory.getLogger(FavouriteProductServiceImpl.class);

    private final FavouriteProductRepository favouriteProductRepository;

    public FavouriteProductServiceImpl(FavouriteProductRepository favouriteProductRepository) {
        this.favouriteProductRepository = favouriteProductRepository;
    }

    @Override
    public FavouriteProduct save(FavouriteProduct favouriteProduct) {
        log.debug("Request to save FavouriteProduct : {}", favouriteProduct);
        return favouriteProductRepository.save(favouriteProduct);
    }

    @Override
    public Optional<FavouriteProduct> partialUpdate(FavouriteProduct favouriteProduct) {
        log.debug("Request to partially update FavouriteProduct : {}", favouriteProduct);

        return favouriteProductRepository
            .findById(favouriteProduct.getId())
            .map(existingFavouriteProduct -> {
                return existingFavouriteProduct;
            })
            .map(favouriteProductRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavouriteProduct> findAll() {
        log.debug("Request to get all FavouriteProducts");
        return favouriteProductRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FavouriteProduct> findOne(Long id) {
        log.debug("Request to get FavouriteProduct : {}", id);
        return favouriteProductRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FavouriteProduct : {}", id);
        favouriteProductRepository.deleteById(id);
    }

    /** Dopisane przez nas */
    @Override
    public List<FavouriteProduct> findAllByUser(String user) {
        log.debug("Request to get FavouriteProduct by user : {}", user);
        return favouriteProductRepository.findByUserIsCurrentUser(user);
    }
}
