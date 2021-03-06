package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Transaction;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Transaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    @Query("select jhiTransaction from Transaction jhiTransaction where jhiTransaction.user.login = ?#{principal.username}")
    Page<Transaction> findByUserIsCurrentUser(Pageable pageable);

    @Query("select jhiTransaction from Transaction jhiTransaction where jhiTransaction.seller.login = ?#{principal.username}")
    Page<Transaction> findAllBySeller(Pageable pageable);
}
