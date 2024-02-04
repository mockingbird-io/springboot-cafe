package com.mockingbird.Springbootcafe.comparator;

import com.mockingbird.Springbootcafe.pojo.Product;

import java.util.Comparator;

public class ProductDateComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return p1.getCreateDate().compareTo(p2.getCreateDate());
    }
}
