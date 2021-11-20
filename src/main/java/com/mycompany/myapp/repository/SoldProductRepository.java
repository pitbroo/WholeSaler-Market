package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SoldProduct;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SoldProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SoldProductRepository extends JpaRepository<SoldProduct, Long>, JpaSpecificationExecutor<SoldProduct> {
    @Query("select soldProduct from SoldProduct soldProduct where soldProduct.user.login = ?#{principal.username}")
    List<SoldProduct> findByUserIsCurrentUser();
}
