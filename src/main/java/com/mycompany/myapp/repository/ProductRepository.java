package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Product;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query("select product from Product product where product.user.login = ?#{principal.username}")
    List<Product> findByUserIsCurrentUser();
}
