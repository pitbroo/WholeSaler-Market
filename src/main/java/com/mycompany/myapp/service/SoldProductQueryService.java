package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.SoldProduct;
import com.mycompany.myapp.repository.SoldProductRepository;
import com.mycompany.myapp.service.criteria.SoldProductCriteria;
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
 * Service for executing complex queries for {@link SoldProduct} entities in the database.
 * The main input is a {@link SoldProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SoldProduct} or a {@link Page} of {@link SoldProduct} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SoldProductQueryService extends QueryService<SoldProduct> {

    private final Logger log = LoggerFactory.getLogger(SoldProductQueryService.class);

    private final SoldProductRepository soldProductRepository;

    public SoldProductQueryService(SoldProductRepository soldProductRepository) {
        this.soldProductRepository = soldProductRepository;
    }

    /**
     * Return a {@link List} of {@link SoldProduct} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SoldProduct> findByCriteria(SoldProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SoldProduct> specification = createSpecification(criteria);
        return soldProductRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SoldProduct} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SoldProduct> findByCriteria(SoldProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SoldProduct> specification = createSpecification(criteria);
        return soldProductRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SoldProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SoldProduct> specification = createSpecification(criteria);
        return soldProductRepository.count(specification);
    }

    /**
     * Function to convert {@link SoldProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SoldProduct> createSpecification(SoldProductCriteria criteria) {
        Specification<SoldProduct> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SoldProduct_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), SoldProduct_.date));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), SoldProduct_.price));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(SoldProduct_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductId(), root -> root.join(SoldProduct_.product, JoinType.LEFT).get(Product_.id))
                    );
            }
        }
        return specification;
    }
}
