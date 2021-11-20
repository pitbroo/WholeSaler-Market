package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Product} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter price;

    private StringFilter seller;

    private LongFilter boughtProductId;

    private LongFilter soldProductId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.seller = other.seller == null ? null : other.seller.copy();
        this.boughtProductId = other.boughtProductId == null ? null : other.boughtProductId.copy();
        this.soldProductId = other.soldProductId == null ? null : other.soldProductId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getPrice() {
        return price;
    }

    public LongFilter price() {
        if (price == null) {
            price = new LongFilter();
        }
        return price;
    }

    public void setPrice(LongFilter price) {
        this.price = price;
    }

    public StringFilter getSeller() {
        return seller;
    }

    public StringFilter seller() {
        if (seller == null) {
            seller = new StringFilter();
        }
        return seller;
    }

    public void setSeller(StringFilter seller) {
        this.seller = seller;
    }

    public LongFilter getBoughtProductId() {
        return boughtProductId;
    }

    public LongFilter boughtProductId() {
        if (boughtProductId == null) {
            boughtProductId = new LongFilter();
        }
        return boughtProductId;
    }

    public void setBoughtProductId(LongFilter boughtProductId) {
        this.boughtProductId = boughtProductId;
    }

    public LongFilter getSoldProductId() {
        return soldProductId;
    }

    public LongFilter soldProductId() {
        if (soldProductId == null) {
            soldProductId = new LongFilter();
        }
        return soldProductId;
    }

    public void setSoldProductId(LongFilter soldProductId) {
        this.soldProductId = soldProductId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(price, that.price) &&
            Objects.equals(seller, that.seller) &&
            Objects.equals(boughtProductId, that.boughtProductId) &&
            Objects.equals(soldProductId, that.soldProductId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, seller, boughtProductId, soldProductId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (seller != null ? "seller=" + seller + ", " : "") +
            (boughtProductId != null ? "boughtProductId=" + boughtProductId + ", " : "") +
            (soldProductId != null ? "soldProductId=" + soldProductId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
