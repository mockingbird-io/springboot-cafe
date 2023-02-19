package com.mockingbird.tmall.web;

import com.mockingbird.tmall.pojo.Product;
import com.mockingbird.tmall.pojo.ProductImage;
import com.mockingbird.tmall.service.CategoryService;
import com.mockingbird.tmall.service.ProductImageService;
import com.mockingbird.tmall.service.ProductService;
import com.mockingbird.tmall.util.Page4Navigator;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@RestController
public class ProductController {
    @Resource
    ProductService productService;
    @Resource
    CategoryService categoryService;
    @Resource
    ProductImageService productImageService;

    @GetMapping("/categories/{cid}/products")
    public Page4Navigator<Product> list(@PathVariable("cid") int cid, @RequestParam(value = "start",defaultValue = "0") int start, @RequestParam(value = "count",defaultValue = "5") int size) throws Exception{
        start = start < 0 ? 0 : start;
        Page4Navigator<Product> list = productService.list(cid,start,size,5);
        productImageService.setFirstProductImages(list.getContent());
        return list;
    }

    @GetMapping("/products/{id}")
    public Product get(@PathVariable("id") int cid) throws Exception{
        return productService.get(cid);
    }

    @PostMapping("/products")
    public Object add(@RequestBody Product product) throws Exception{
        product.setCreateDate(new Date());
        productService.add(product);
        return product;
    }

    @DeleteMapping("products/{id}")
    public void delete(@PathVariable("id") int id) throws Exception{
        productService.delete(id);
    }

    @PutMapping("products")
    public Object update(@RequestBody Product product) throws Exception{
        productService.update(product);
        return product;
    }
}
