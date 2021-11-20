package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BoughtProduct;
import com.mycompany.myapp.repository.BoughtProductRepository;
import com.mycompany.myapp.service.BoughtProductQueryService;
import com.mycompany.myapp.service.BoughtProductService;
import com.mycompany.myapp.service.criteria.BoughtProductCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BoughtProduct}.
 */
@RestController
@RequestMapping("/api")
public class BoughtProductResource {

    private final Logger log = LoggerFactory.getLogger(BoughtProductResource.class);

    private static final String ENTITY_NAME = "boughtProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoughtProductService boughtProductService;

    private final BoughtProductRepository boughtProductRepository;

    private final BoughtProductQueryService boughtProductQueryService;

    public BoughtProductResource(
        BoughtProductService boughtProductService,
        BoughtProductRepository boughtProductRepository,
        BoughtProductQueryService boughtProductQueryService
    ) {
        this.boughtProductService = boughtProductService;
        this.boughtProductRepository = boughtProductRepository;
        this.boughtProductQueryService = boughtProductQueryService;
    }

    /**
     * {@code POST  /bought-products} : Create a new boughtProduct.
     *
     * @param boughtProduct the boughtProduct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new boughtProduct, or with status {@code 400 (Bad Request)} if the boughtProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bought-products")
    public ResponseEntity<BoughtProduct> createBoughtProduct(@RequestBody BoughtProduct boughtProduct) throws URISyntaxException {
        log.debug("REST request to save BoughtProduct : {}", boughtProduct);
        if (boughtProduct.getId() != null) {
            throw new BadRequestAlertException("A new boughtProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BoughtProduct result = boughtProductService.save(boughtProduct);
        return ResponseEntity
            .created(new URI("/api/bought-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bought-products/:id} : Updates an existing boughtProduct.
     *
     * @param id the id of the boughtProduct to save.
     * @param boughtProduct the boughtProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boughtProduct,
     * or with status {@code 400 (Bad Request)} if the boughtProduct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the boughtProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bought-products/{id}")
    public ResponseEntity<BoughtProduct> updateBoughtProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BoughtProduct boughtProduct
    ) throws URISyntaxException {
        log.debug("REST request to update BoughtProduct : {}, {}", id, boughtProduct);
        if (boughtProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boughtProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boughtProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BoughtProduct result = boughtProductService.save(boughtProduct);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boughtProduct.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bought-products/:id} : Partial updates given fields of an existing boughtProduct, field will ignore if it is null
     *
     * @param id the id of the boughtProduct to save.
     * @param boughtProduct the boughtProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boughtProduct,
     * or with status {@code 400 (Bad Request)} if the boughtProduct is not valid,
     * or with status {@code 404 (Not Found)} if the boughtProduct is not found,
     * or with status {@code 500 (Internal Server Error)} if the boughtProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bought-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BoughtProduct> partialUpdateBoughtProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BoughtProduct boughtProduct
    ) throws URISyntaxException {
        log.debug("REST request to partial update BoughtProduct partially : {}, {}", id, boughtProduct);
        if (boughtProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boughtProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boughtProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BoughtProduct> result = boughtProductService.partialUpdate(boughtProduct);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boughtProduct.getId().toString())
        );
    }

    /**
     * {@code GET  /bought-products} : get all the boughtProducts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boughtProducts in body.
     */
    @GetMapping("/bought-products")
    public ResponseEntity<List<BoughtProduct>> getAllBoughtProducts(BoughtProductCriteria criteria) {
        log.debug("REST request to get BoughtProducts by criteria: {}", criteria);
        List<BoughtProduct> entityList = boughtProductQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /bought-products/count} : count all the boughtProducts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bought-products/count")
    public ResponseEntity<Long> countBoughtProducts(BoughtProductCriteria criteria) {
        log.debug("REST request to count BoughtProducts by criteria: {}", criteria);
        return ResponseEntity.ok().body(boughtProductQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bought-products/:id} : get the "id" boughtProduct.
     *
     * @param id the id of the boughtProduct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the boughtProduct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bought-products/{id}")
    public ResponseEntity<BoughtProduct> getBoughtProduct(@PathVariable Long id) {
        log.debug("REST request to get BoughtProduct : {}", id);
        Optional<BoughtProduct> boughtProduct = boughtProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(boughtProduct);
    }

    /**
     * {@code DELETE  /bought-products/:id} : delete the "id" boughtProduct.
     *
     * @param id the id of the boughtProduct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bought-products/{id}")
    public ResponseEntity<Void> deleteBoughtProduct(@PathVariable Long id) {
        log.debug("REST request to delete BoughtProduct : {}", id);
        boughtProductService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
