package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.FavouriteProduct;
import com.mycompany.myapp.repository.FavouriteProductRepository;
import com.mycompany.myapp.service.criteria.FavouriteProductCriteria;
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
 * Service for executing complex queries for {@link FavouriteProduct} entities in the database.
 * The main input is a {@link FavouriteProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FavouriteProduct} or a {@link Page} of {@link FavouriteProduct} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FavouriteProductQueryService extends QueryService<FavouriteProduct> {

    private final Logger log = LoggerFactory.getLogger(FavouriteProductQueryService.class);

    private final FavouriteProductRepository favouriteProductRepository;

    public FavouriteProductQueryService(FavouriteProductRepository favouriteProductRepository) {
        this.favouriteProductRepository = favouriteProductRepository;
    }

    /**
     * Return a {@link List} of {@link FavouriteProduct} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FavouriteProduct> findByCriteria(FavouriteProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FavouriteProduct> specification = createSpecification(criteria);
        return favouriteProductRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link FavouriteProduct} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FavouriteProduct> findByCriteria(FavouriteProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FavouriteProduct> specification = createSpecification(criteria);
        return favouriteProductRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FavouriteProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FavouriteProduct> specification = createSpecification(criteria);
        return favouriteProductRepository.count(specification);
    }

    /**
     * Function to convert {@link FavouriteProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FavouriteProduct> createSpecification(FavouriteProductCriteria criteria) {
        Specification<FavouriteProduct> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FavouriteProduct_.id));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(FavouriteProduct_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(FavouriteProduct_.product, JoinType.LEFT).get(Product_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
