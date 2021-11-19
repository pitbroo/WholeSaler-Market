package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FavouriteProduct;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FavouriteProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavouriteProductRepository extends JpaRepository<FavouriteProduct, Long>, JpaSpecificationExecutor<FavouriteProduct> {
    @Query("select favouriteProduct from FavouriteProduct favouriteProduct where favouriteProduct.user.login = ?#{principal.username}")
    Page<FavouriteProduct> findByUserIsCurrentUser(Pageable pageable);
}
