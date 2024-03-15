package com.mockingbird.Springbootcafe.web;

import com.mockingbird.Springbootcafe.comparator.*;
import com.mockingbird.Springbootcafe.pojo.*;
import com.mockingbird.Springbootcafe.service.*;
import com.mockingbird.Springbootcafe.util.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ForeRestController {
    @Resource
    CategoryService categoryService;
    @Resource
    ProductService productService;
    @Resource
    UserService userService;
    @Resource
    ProductImageService productImageService;
    @Resource
    PropertyValueService propertyValueService;
    @Resource
    ReviewService reviewService;
    @Resource
    OrderItemService orderItemService;

    @GetMapping("/forehome")
    public Object home() {
        List<Category> list = categoryService.list();
        productService.fill(list);
        productService.fillByRow(list);
        categoryService.removeCategoryFromProduct(list);
        return list;
    }

    @PostMapping("/foreregister")
    public Object register(@RequestBody Users users) {
        String name = HtmlUtils.htmlEscape(users.getName());
        users.setName(name);
        boolean exist = userService.isExist(name);
        if (exist) {
            String message = "用户名已经被使用,不能使用";
            return Result.fail(message);
        }
        userService.add(users);
        return Result.success();
    }

    @PostMapping("/forelogin")
    public Object login(@RequestBody Users userParam, HttpSession session) {
        String name = userParam.getName();
        name = HtmlUtils.htmlEscape(name);

        Users user = userService.get(name, userParam.getPassword());
        if (user == null) {
            String message = "账号密码错误";
            return Result.fail(message);
        } else {
            session.setAttribute("user", user);
            return Result.success();
        }
    }

    @GetMapping("/foreproduct/{pid}")
    public Object product(@PathVariable Integer pid) {
        Product product = productService.get(pid);

        List<ProductImage> productSingleImages = productImageService.listSingleProductImages(product);
        List<ProductImage> productDetailImages = productImageService.listDetailProductImages(product);
        product.setProductSingleImages(productSingleImages);
        product.setProductDetailImages(productDetailImages);

        List<PropertyValue> pvs = propertyValueService.list(product);
        List<Review> reviews = reviewService.list(product);
        productService.setSaleAndReviewNumber(product);
        productImageService.setFirstProductImage(product);

        Map<String, Object> map = new HashMap<>();
        map.put("product", product);
        map.put("pvs", pvs);
        map.put("reviews", reviews);

        return Result.success(map);
    }

    @GetMapping("forecheckLogin")
    public Object checkLogin(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (null != user) {
            return Result.success();
        }
        return Result.fail("未登录");
    }

    @GetMapping("forecategory/{cid}")
    public Object category(@PathVariable int cid, String sort) {
        Category c = categoryService.get(cid);
        productService.fill(c);
        productService.setSaleAndReviewNumber(c.getProducts());
        categoryService.removeCategoryFromProduct(c);

        if (null != sort) {
            switch (sort) {
                case "review":
                    c.getProducts().sort(new ProductReviewComparator());
                    break;
                case "date":
                    c.getProducts().sort(new ProductDateComparator());
                    break;

                case "saleCount":
                    c.getProducts().sort(new ProductSaleCountComparator());
                    break;

                case "price":
                    c.getProducts().sort(new ProductPriceComparator());
                    break;

                case "all":
                    c.getProducts().sort(new ProductAllcomparator());
                    break;
            }
        }
        return c;
    }

    @PostMapping("foresearch")
    public Object search(String keyword) {
        if (null == keyword)
            keyword = "";
        List<Product> ps = productService.search(keyword, 0, 20);
        productImageService.setFirstProductImages(ps);
        productService.setSaleAndReviewNumber(ps);
        return ps;
    }

    @GetMapping("forebuyone")
    public Object buyOne(int pid, int num, HttpSession session) {
        return buyOneAndAddCart(pid, num, session);
    }

    private int buyOneAndAddCart(int pid, int num, HttpSession session) {
        Product product = productService.get(pid);
        int oiid = 0;

        Users users =(Users)  session.getAttribute("user");
        boolean found = false;
        List<OrderItem> ois = orderItemService.listByUsers(users);
        for (OrderItem oi : ois) {
            if(oi.getProduct().getId()==product.getId()){
                oi.setNumber(oi.getNumber()+num);
                orderItemService.update(oi);
                found = true;
                oiid = oi.getId();
                break;
            }
        }

        if(!found){
            OrderItem oi = new OrderItem();
            oi.setUsers(users);
            oi.setProduct(product);
            oi.setNumber(num);
            orderItemService.add(oi);
            oiid = oi.getId();
        }
        return oiid;
    }

    @GetMapping("forebuy")
    public Object buy(String[] oiid,HttpSession session){
        List<OrderItem> orderItems = new ArrayList<>();
        float total = 0;

        for (String strid : oiid) {
            int id = Integer.parseInt(strid);
            OrderItem oi= orderItemService.get(id);
            total +=oi.getProduct().getPromotePrice()*oi.getNumber();
            orderItems.add(oi);
        }

        productImageService.setFirstProductImagesOnOrderItems(orderItems);

        session.setAttribute("ois", orderItems);

        Map<String,Object> map = new HashMap<>();
        map.put("orderItems", orderItems);
        map.put("total", total);
        return Result.success(map);
    }

    @GetMapping("foreaddCart")
    public Object addCart(int pid, int num, HttpSession session) {
        buyOneAndAddCart(pid,num,session);
        return Result.success();
    }

    @GetMapping("forecart")
    public Object cart(HttpSession session) {
        Users user =(Users)  session.getAttribute("user");
        List<OrderItem> ois = orderItemService.listByUsers(user);
        productImageService.setFirstProductImagesOnOrderItems(ois);
        return ois;
    }

}
