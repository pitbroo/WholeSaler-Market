package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.FavouriteProduct;
import com.mycompany.myapp.repository.FavouriteProductRepository;
import com.mycompany.myapp.service.FavouriteProductQueryService;
import com.mycompany.myapp.service.FavouriteProductService;
import com.mycompany.myapp.service.criteria.FavouriteProductCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.FavouriteProduct}.
 */
@RestController
@RequestMapping("/api")
public class FavouriteProductResource {

    private final Logger log = LoggerFactory.getLogger(FavouriteProductResource.class);

    private static final String ENTITY_NAME = "favouriteProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FavouriteProductService favouriteProductService;

    private final FavouriteProductRepository favouriteProductRepository;

    private final FavouriteProductQueryService favouriteProductQueryService;

    public FavouriteProductResource(
        FavouriteProductService favouriteProductService,
        FavouriteProductRepository favouriteProductRepository,
        FavouriteProductQueryService favouriteProductQueryService
    ) {
        this.favouriteProductService = favouriteProductService;
        this.favouriteProductRepository = favouriteProductRepository;
        this.favouriteProductQueryService = favouriteProductQueryService;
    }

    /**
     * {@code POST  /favourite-products} : Create a new favouriteProduct.
     *
     * @param favouriteProduct the favouriteProduct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new favouriteProduct, or with status {@code 400 (Bad Request)} if the favouriteProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/favourite-products")
    public ResponseEntity<FavouriteProduct> createFavouriteProduct(@RequestBody FavouriteProduct favouriteProduct)
        throws URISyntaxException {
        log.debug("REST request to save FavouriteProduct : {}", favouriteProduct);
        if (favouriteProduct.getId() != null) {
            throw new BadRequestAlertException("A new favouriteProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FavouriteProduct result = favouriteProductService.save(favouriteProduct);
        return ResponseEntity
            .created(new URI("/api/favourite-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /favourite-products/:id} : Updates an existing favouriteProduct.
     *
     * @param id the id of the favouriteProduct to save.
     * @param favouriteProduct the favouriteProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated favouriteProduct,
     * or with status {@code 400 (Bad Request)} if the favouriteProduct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the favouriteProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/favourite-products/{id}")
    public ResponseEntity<FavouriteProduct> updateFavouriteProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FavouriteProduct favouriteProduct
    ) throws URISyntaxException {
        log.debug("REST request to update FavouriteProduct : {}, {}", id, favouriteProduct);
        if (favouriteProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, favouriteProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!favouriteProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FavouriteProduct result = favouriteProductService.save(favouriteProduct);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, favouriteProduct.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /favourite-products/:id} : Partial updates given fields of an existing favouriteProduct, field will ignore if it is null
     *
     * @param id the id of the favouriteProduct to save.
     * @param favouriteProduct the favouriteProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated favouriteProduct,
     * or with status {@code 400 (Bad Request)} if the favouriteProduct is not valid,
     * or with status {@code 404 (Not Found)} if the favouriteProduct is not found,
     * or with status {@code 500 (Internal Server Error)} if the favouriteProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/favourite-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FavouriteProduct> partialUpdateFavouriteProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FavouriteProduct favouriteProduct
    ) throws URISyntaxException {
        log.debug("REST request to partial update FavouriteProduct partially : {}, {}", id, favouriteProduct);
        if (favouriteProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, favouriteProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!favouriteProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FavouriteProduct> result = favouriteProductService.partialUpdate(favouriteProduct);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, favouriteProduct.getId().toString())
        );
    }

    /**
     * {@code GET  /favourite-products} : get all the favouriteProducts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of favouriteProducts in body.
     */
    @GetMapping("/favourite-products")
    public ResponseEntity<List<FavouriteProduct>> getAllFavouriteProducts(FavouriteProductCriteria criteria) {
        log.debug("REST request to get FavouriteProducts by criteria: {}", criteria);
        List<FavouriteProduct> entityList = favouriteProductQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /favourite-products/count} : count all the favouriteProducts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/favourite-products/count")
    public ResponseEntity<Long> countFavouriteProducts(FavouriteProductCriteria criteria) {
        log.debug("REST request to count FavouriteProducts by criteria: {}", criteria);
        return ResponseEntity.ok().body(favouriteProductQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /favourite-products/:id} : get the "id" favouriteProduct.
     *
     * @param id the id of the favouriteProduct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the favouriteProduct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/favourite-products/{id}")
    public ResponseEntity<FavouriteProduct> getFavouriteProduct(@PathVariable Long id) {
        log.debug("REST request to get FavouriteProduct : {}", id);
        Optional<FavouriteProduct> favouriteProduct = favouriteProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(favouriteProduct);
    }

    /**
     * {@code DELETE  /favourite-products/:id} : delete the "id" favouriteProduct.
     *
     * @param id the id of the favouriteProduct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/favourite-products/{id}")
    public ResponseEntity<Void> deleteFavouriteProduct(@PathVariable Long id) {
        log.debug("REST request to delete FavouriteProduct : {}", id);
        favouriteProductService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
