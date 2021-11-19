package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FavouriteProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the FavouriteProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavouriteProductRepository extends JpaRepository<FavouriteProduct, Long> {
    List<FavouriteProduct> findAllById(Long userId);
}
