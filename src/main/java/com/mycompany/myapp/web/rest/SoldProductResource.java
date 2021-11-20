package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SoldProduct;
import com.mycompany.myapp.repository.SoldProductRepository;
import com.mycompany.myapp.service.SoldProductQueryService;
import com.mycompany.myapp.service.SoldProductService;
import com.mycompany.myapp.service.criteria.SoldProductCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SoldProduct}.
 */
@RestController
@RequestMapping("/api")
public class SoldProductResource {

    private final Logger log = LoggerFactory.getLogger(SoldProductResource.class);

    private static final String ENTITY_NAME = "soldProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SoldProductService soldProductService;

    private final SoldProductRepository soldProductRepository;

    private final SoldProductQueryService soldProductQueryService;

    public SoldProductResource(
        SoldProductService soldProductService,
        SoldProductRepository soldProductRepository,
        SoldProductQueryService soldProductQueryService
    ) {
        this.soldProductService = soldProductService;
        this.soldProductRepository = soldProductRepository;
        this.soldProductQueryService = soldProductQueryService;
    }

    /**
     * {@code POST  /sold-products} : Create a new soldProduct.
     *
     * @param soldProduct the soldProduct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new soldProduct, or with status {@code 400 (Bad Request)} if the soldProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sold-products")
    public ResponseEntity<SoldProduct> createSoldProduct(@RequestBody SoldProduct soldProduct) throws URISyntaxException {
        log.debug("REST request to save SoldProduct : {}", soldProduct);
        if (soldProduct.getId() != null) {
            throw new BadRequestAlertException("A new soldProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SoldProduct result = soldProductService.save(soldProduct);
        return ResponseEntity
            .created(new URI("/api/sold-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sold-products/:id} : Updates an existing soldProduct.
     *
     * @param id the id of the soldProduct to save.
     * @param soldProduct the soldProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated soldProduct,
     * or with status {@code 400 (Bad Request)} if the soldProduct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the soldProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sold-products/{id}")
    public ResponseEntity<SoldProduct> updateSoldProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SoldProduct soldProduct
    ) throws URISyntaxException {
        log.debug("REST request to update SoldProduct : {}, {}", id, soldProduct);
        if (soldProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, soldProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!soldProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SoldProduct result = soldProductService.save(soldProduct);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, soldProduct.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sold-products/:id} : Partial updates given fields of an existing soldProduct, field will ignore if it is null
     *
     * @param id the id of the soldProduct to save.
     * @param soldProduct the soldProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated soldProduct,
     * or with status {@code 400 (Bad Request)} if the soldProduct is not valid,
     * or with status {@code 404 (Not Found)} if the soldProduct is not found,
     * or with status {@code 500 (Internal Server Error)} if the soldProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sold-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SoldProduct> partialUpdateSoldProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SoldProduct soldProduct
    ) throws URISyntaxException {
        log.debug("REST request to partial update SoldProduct partially : {}, {}", id, soldProduct);
        if (soldProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, soldProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!soldProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SoldProduct> result = soldProductService.partialUpdate(soldProduct);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, soldProduct.getId().toString())
        );
    }

    /**
     * {@code GET  /sold-products} : get all the soldProducts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of soldProducts in body.
     */
    @GetMapping("/sold-products")
    public ResponseEntity<List<SoldProduct>> getAllSoldProducts(SoldProductCriteria criteria) {
        log.debug("REST request to get SoldProducts by criteria: {}", criteria);
        List<SoldProduct> entityList = soldProductQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /sold-products/count} : count all the soldProducts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sold-products/count")
    public ResponseEntity<Long> countSoldProducts(SoldProductCriteria criteria) {
        log.debug("REST request to count SoldProducts by criteria: {}", criteria);
        return ResponseEntity.ok().body(soldProductQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sold-products/:id} : get the "id" soldProduct.
     *
     * @param id the id of the soldProduct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the soldProduct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sold-products/{id}")
    public ResponseEntity<SoldProduct> getSoldProduct(@PathVariable Long id) {
        log.debug("REST request to get SoldProduct : {}", id);
        Optional<SoldProduct> soldProduct = soldProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(soldProduct);
    }

    /**
     * {@code DELETE  /sold-products/:id} : delete the "id" soldProduct.
     *
     * @param id the id of the soldProduct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sold-products/{id}")
    public ResponseEntity<Void> deleteSoldProduct(@PathVariable Long id) {
        log.debug("REST request to delete SoldProduct : {}", id);
        soldProductService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
