package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Long price;

    @Column(name = "seller")
    private String seller;

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "product" }, allowSetters = true)
    private Set<BoughtProduct> boughtProducts = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "product" }, allowSetters = true)
    private Set<SoldProduct> soldProducts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return this.price;
    }

    public Product price(Long price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getSeller() {
        return this.seller;
    }

    public Product seller(String seller) {
        this.setSeller(seller);
        return this;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public Set<BoughtProduct> getBoughtProducts() {
        return this.boughtProducts;
    }

    public void setBoughtProducts(Set<BoughtProduct> boughtProducts) {
        if (this.boughtProducts != null) {
            this.boughtProducts.forEach(i -> i.setProduct(null));
        }
        if (boughtProducts != null) {
            boughtProducts.forEach(i -> i.setProduct(this));
        }
        this.boughtProducts = boughtProducts;
    }

    public Product boughtProducts(Set<BoughtProduct> boughtProducts) {
        this.setBoughtProducts(boughtProducts);
        return this;
    }

    public Product addBoughtProduct(BoughtProduct boughtProduct) {
        this.boughtProducts.add(boughtProduct);
        boughtProduct.setProduct(this);
        return this;
    }

    public Product removeBoughtProduct(BoughtProduct boughtProduct) {
        this.boughtProducts.remove(boughtProduct);
        boughtProduct.setProduct(null);
        return this;
    }

    public Set<SoldProduct> getSoldProducts() {
        return this.soldProducts;
    }

    public void setSoldProducts(Set<SoldProduct> soldProducts) {
        if (this.soldProducts != null) {
            this.soldProducts.forEach(i -> i.setProduct(null));
        }
        if (soldProducts != null) {
            soldProducts.forEach(i -> i.setProduct(this));
        }
        this.soldProducts = soldProducts;
    }

    public Product soldProducts(Set<SoldProduct> soldProducts) {
        this.setSoldProducts(soldProducts);
        return this;
    }

    public Product addSoldProduct(SoldProduct soldProduct) {
        this.soldProducts.add(soldProduct);
        soldProduct.setProduct(this);
        return this;
    }

    public Product removeSoldProduct(SoldProduct soldProduct) {
        this.soldProducts.remove(soldProduct);
        soldProduct.setProduct(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", seller='" + getSeller() + "'" +
            "}";
    }
}
