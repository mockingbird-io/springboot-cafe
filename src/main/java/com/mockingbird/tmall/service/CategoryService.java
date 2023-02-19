package com.mockingbird.tmall.service;

import java.util.List;

import com.mockingbird.tmall.pojo.Product;
import com.mockingbird.tmall.util.Page4Navigator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mockingbird.tmall.dao.CategoryDAO;
import com.mockingbird.tmall.pojo.Category;

import javax.annotation.Resource;

@Service
public class CategoryService {
	@Resource
	CategoryDAO categoryDAO;

	public Page4Navigator<Category> list(int start,int size, int navigatePages){
		Sort sort = Sort.by(Sort.Direction.DESC,"id");
		Pageable pageable = PageRequest.of(start,size,sort);

		Page<Category> pageFromJPA = categoryDAO.findAll(pageable);

		return new Page4Navigator<>(pageFromJPA,navigatePages);

	}

	public List<Category> list() {
		return categoryDAO.findAll(Sort.by(Sort.Direction.DESC, "id"));
	}

	public void add(Category bean){
		categoryDAO.save(bean);
	}

	public void delete(int id){
		categoryDAO.deleteById(id);
	}

	public Category get(int id){
		return categoryDAO.findById(id).orElse(null);
	}

	public void update(Category category){
		categoryDAO.save(category);
	}

	public void removeCategoryFromProduct(List<Category> cs){
		for (Category category : cs){
			removeCategoryFromProduct(category);
		}
	}

	public void removeCategoryFromProduct(Category category) {
		List<Product> products =category.getProducts();
		if(null!=products) {
			for (Product product : products) {
				product.setCategory(null);
			}
		}

		List<List<Product>> productsByRow =category.getProductsByRow();
		if(null!=productsByRow) {
			for (List<Product> ps : productsByRow) {
				for (Product p: ps) {
					p.setCategory(null);
				}
			}
		}
	}
}
