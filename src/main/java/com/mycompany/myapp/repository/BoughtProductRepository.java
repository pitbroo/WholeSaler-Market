package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BoughtProduct;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BoughtProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoughtProductRepository extends JpaRepository<BoughtProduct, Long>, JpaSpecificationExecutor<BoughtProduct> {
    @Query("select boughtProduct from BoughtProduct boughtProduct where boughtProduct.user.login = ?#{principal.username}")
    List<BoughtProduct> findByUserIsCurrentUser();
}
