package com.mockingbird.Springbootcafe.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
//@Document(indexName = "tmall_springboot")
@Data
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

    @Getter
    @Transient
    private ProductImage firstProductImage;
    @Transient
    private List<ProductImage> productSingleImages;
    @Transient
    private List<ProductImage> productDetailImages;
    @Transient
    private int reviewCount;
    @Transient
    private int saleCount;

    @Override
    public String toString() {
        return "Product [id=" + id + ", category=" + category + ", name=" + name + ", subTitle=" + subTitle
                + ", originalPrice=" + originalPrice + ", promotePrice=" + promotePrice + ", stock=" + stock
                + ", createDate=" + createDate + ", firstProductImage=" + firstProductImage + ", reviewCount="
                + reviewCount + ", saleCount=" + saleCount + "]";
    }
}
