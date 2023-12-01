package com.mockingbird.Springbootcafe.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "product")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
//@Document(indexName = "tmall_springboot")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "cid")
    private Category category;

    private String name;
    @Column(name = "sub_title")
    private String subTitle;
    @Column(name = "original_price")
    private float originalPrice;
    @Column(name = "promote_price")
    private float promotePrice;
    private int stock;
    @Column(name = "create_date")
    private Date createDate;

    @Transient
    private ProductImage productImage;

    public ProductImage getFirstProductImage() {
        return productImage;
    }

    public void setFirstProductImage(ProductImage productImage){
        this.productImage = productImage;
    }
}
