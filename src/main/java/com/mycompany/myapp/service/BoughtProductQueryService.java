package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.BoughtProduct;
import com.mycompany.myapp.repository.BoughtProductRepository;
import com.mycompany.myapp.service.criteria.BoughtProductCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link BoughtProduct} entities in the database.
 * The main input is a {@link BoughtProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BoughtProduct} or a {@link Page} of {@link BoughtProduct} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BoughtProductQueryService extends QueryService<BoughtProduct> {

    private final Logger log = LoggerFactory.getLogger(BoughtProductQueryService.class);

    private final BoughtProductRepository boughtProductRepository;

    public BoughtProductQueryService(BoughtProductRepository boughtProductRepository) {
        this.boughtProductRepository = boughtProductRepository;
    }

    /**
     * Return a {@link List} of {@link BoughtProduct} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BoughtProduct> findByCriteria(BoughtProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BoughtProduct> specification = createSpecification(criteria);
        return boughtProductRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BoughtProduct} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BoughtProduct> findByCriteria(BoughtProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BoughtProduct> specification = createSpecification(criteria);
        return boughtProductRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BoughtProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BoughtProduct> specification = createSpecification(criteria);
        return boughtProductRepository.count(specification);
    }

    /**
     * Function to convert {@link BoughtProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BoughtProduct> createSpecification(BoughtProductCriteria criteria) {
        Specification<BoughtProduct> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BoughtProduct_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), BoughtProduct_.date));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), BoughtProduct_.price));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(BoughtProduct_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(BoughtProduct_.product, JoinType.LEFT).get(Product_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
