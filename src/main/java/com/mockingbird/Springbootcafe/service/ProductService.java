package com.mockingbird.Springbootcafe.service;

import com.mockingbird.Springbootcafe.dao.ProductDAO;
import com.mockingbird.Springbootcafe.pojo.Category;
import com.mockingbird.Springbootcafe.pojo.Product;
import com.mockingbird.Springbootcafe.util.Page4Navigator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Resource
    ProductDAO productDAO;
    @Resource
    CategoryService categoryService;
    @Resource
    ProductImageService productImageService;

    public void add(Product product){
        productDAO.save(product);
    }

    public void delete(int id){
        productDAO.deleteById(id);
    }

    public void update(Product product) {
        productDAO.save(product);
    }

    public Product get(int id){
        return productDAO.findById(id).orElse(null);
    }

    public Page4Navigator<Product> list(int cid, int start, int count, int navigatePages){
        Category category = categoryService.get(cid);
        Pageable pageable = PageRequest.of(start,count, Sort.by(Sort.Direction.DESC,"id"));
        Page<Product> pageFromJpa = productDAO.findByCategory(category,pageable);
        return new Page4Navigator<>(pageFromJpa,navigatePages);
    }

    public void fill(List<Category> categoryList){
        for (Category category : categoryList){
            fill(category);
        }
    }

    public void fill(Category category){
        List<Product> products = productDAO.findByCategoryOrderById(category);
        productImageService.setFirstProductImages(products);
        category.setProducts(products);
    }

    public void fillByRow(List<Category> categoryList){
        int productNumberEachRow = 8;
        for (Category category : categoryList){
            List<Product> products = productDAO.findByCategoryOrderById(category);
            List<List<Product>> productByRow = new ArrayList<>();
            for (int i = 0; i <= products.size(); i+=productNumberEachRow){
                int size = i + productNumberEachRow;
                size = Math.min(size, products.size());
                List<Product> productsOfEachRow = products.subList(i, size);
                productByRow.add(productsOfEachRow);
            }
            category.setProductsByRow(productByRow);
        }
    }
}
