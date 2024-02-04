package com.mockingbird.Springbootcafe.comparator;

import com.mockingbird.Springbootcafe.pojo.Product;

import java.util.Comparator;

public class ProductAllcomparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return p2.getReviewCount() * p2.getSaleCount() - p1.getReviewCount() * p1.getSaleCount();
    }
}
