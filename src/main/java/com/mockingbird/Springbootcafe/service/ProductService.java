package com.mockingbird.Springbootcafe.service;

import com.mockingbird.Springbootcafe.dao.ProductDAO;
import com.mockingbird.Springbootcafe.es.ProductESDAO;
import com.mockingbird.Springbootcafe.pojo.Category;
import com.mockingbird.Springbootcafe.pojo.Product;
import com.mockingbird.Springbootcafe.util.Page4Navigator;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames="products")
public class ProductService {
    @Resource
    ProductDAO productDAO;
    @Resource
    CategoryService categoryService;
    @Resource
    ProductImageService productImageService;
    @Resource
    OrderItemService orderItemService;
    @Resource
    ReviewService reviewService;
    @Resource
    private ProductESDAO productESDAO;
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @CacheEvict(allEntries=true)
    public void add(Product product) {
        productDAO.save(product);
        productESDAO.save(product);
    }

    @CacheEvict(allEntries=true)
    public void delete(int id) {
        productDAO.deleteById(id);
        productESDAO.deleteById(id);
    }

    @CacheEvict(allEntries=true)
    public void update(Product product) {
        productDAO.save(product);
        productESDAO.save(product);
    }

    private void initDatabase2ES() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> page =productESDAO.findAll(pageable);
        if(page.getContent().isEmpty()) {
            List<Product> products= productDAO.findAll();
            for (Product product : products) {
                productESDAO.save(product);
            }
        }
    }


    @Cacheable(key="'products-one-'+ #p0")
    public Product get(int id) {
        return productDAO.findById(id).orElse(null);
    }

    @Cacheable(key="'products-cid-'+#p0+'-page-'+#p1 + '-' + #p2 ")
    public Page4Navigator<Product> list(int cid, int start, int count, int navigatePages) {
        Category category = categoryService.get(cid);
        Pageable pageable = PageRequest.of(start, count, Sort.by(Sort.Direction.DESC, "id"));
        Page<Product> pageFromJpa = productDAO.findByCategory(category, pageable);
        return new Page4Navigator<>(pageFromJpa, navigatePages);
    }

    public void fill(List<Category> categoryList) {
        for (Category category : categoryList) {
            fill(category);
        }
    }

    public void fill(Category category) {
        List<Product> products = productDAO.findByCategoryOrderById(category);
        productImageService.setFirstProductImages(products);
        category.setProducts(products);

    }

    @Cacheable(key="'products-cid-'+ #p0.id")
    public List<Product> listByCategory(Category category){
        return productDAO.findByCategoryOrderById(category);
    }

    public void fillByRow(List<Category> categoryList) {
        int productNumberEachRow = 8;
        for (Category category : categoryList) {
            List<Product> products = productDAO.findByCategoryOrderById(category);
            List<List<Product>> productByRow = new ArrayList<>();
            for (int i = 0; i <= products.size(); i += productNumberEachRow) {
                int size = i + productNumberEachRow;
                size = Math.min(size, products.size());
                List<Product> productsOfEachRow = products.subList(i, size);
                productByRow.add(productsOfEachRow);
            }
            category.setProductsByRow(productByRow);
        }
    }

    public void setSaleAndReviewNumber(Product product) {
        int saleCount = orderItemService.getSaleCount(product);
        product.setSaleCount(saleCount);

        int reviewCount = reviewService.getCount(product);
        product.setReviewCount(reviewCount);

    }

    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product product : products)
            setSaleAndReviewNumber(product);
    }

    public List<Product> search(String keyword, int start, int size) {
        initDatabase2ES();
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(
                        QueryBuilders.matchPhraseQuery("name", keyword),
                        ScoreFunctionBuilders.weightFactorFunction(100))
                .scoreMode(FunctionScoreQuery.ScoreMode.valueOf("sum"))
                .setMinScore(10);
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        Pageable pageable = PageRequest.of(start, size, sort);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(functionScoreQueryBuilder).build();
        SearchHits<Product> searchHits = elasticsearchRestTemplate.search(searchQuery, Product.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
