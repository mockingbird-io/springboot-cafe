package com.mockingbird.Springbootcafe.web;

import com.mockingbird.Springbootcafe.comparator.*;
import com.mockingbird.Springbootcafe.pojo.*;
import com.mockingbird.Springbootcafe.service.*;
import com.mockingbird.Springbootcafe.util.Result;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Resource
    private OrderService orderService;

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
        String name =  users.getName();
        String password = users.getPassword();
        name = HtmlUtils.htmlEscape(name);
        users.setName(name);

        boolean exist = userService.isExist(name);

        if(exist){
            String message ="用户名已经被使用,不能使用";
            return Result.fail(message);
        }

        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithmName = "md5";

        String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();

        users.setSalt(salt);
        users.setPassword(encodedPassword);

        userService.add(users);

        return Result.success();
    }

    @PostMapping("/forelogin")
    public Object login(@RequestBody Users userParam, HttpSession session) {
        String name =  userParam.getName();
        name = HtmlUtils.htmlEscape(name);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, userParam.getPassword());
        try {
            subject.login(token);
            Users user = userService.getByName(name);
//          subject.getSession().setAttribute("user", user);
            session.setAttribute("user", user);
            return Result.success();
        } catch (AuthenticationException e) {
            String message ="账号密码错误";
            return Result.fail(message);
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

    @GetMapping("forechangeOrderItem")
    public Object changeOrderItem( HttpSession session, int pid, int num) {
        Users user =(Users)  session.getAttribute("user");
        if(null==user)
            return Result.fail("未登录");

        List<OrderItem> ois = orderItemService.listByUser(user);
        for (OrderItem oi : ois) {
            if(oi.getProduct().getId()==pid){
                oi.setNumber(num);
                orderItemService.update(oi);
                break;
            }
        }
        return Result.success();
    }

    @GetMapping("foredeleteOrderItem")
    public Object deleteOrderItem(HttpSession session,int oiid){
        Users user =(Users)  session.getAttribute("user");
        if(null==user)
            return Result.fail("未登录");
        orderItemService.delete(oiid);
        return Result.success();
    }

    @PostMapping("forecreateOrder")
    public Object createOrder(@RequestBody Order order,HttpSession session){
        Users user =(Users)  session.getAttribute("user");
        if(null==user)
            return Result.fail("未登录");
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
        order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        order.setUsers(user);
        order.setStatus(OrderService.waitPay);
        List<OrderItem> ois= (List<OrderItem>)  session.getAttribute("ois");

        float total = orderService.add(order,ois);

        Map<String,Object> map = new HashMap<>();
        map.put("oid", order.getId());
        map.put("total", total);

        return Result.success(map);
    }

    @GetMapping("forepayed")
    public Object payed(int oid) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.waitDelivery);
        order.setPayDate(new Date());
        orderService.update(order);
        return order;
    }

    @GetMapping("forebought")
    public Object bought(HttpSession session) {
        Users user =(Users)  session.getAttribute("user");
        if(null==user)
            return Result.fail("未登录");
        List<Order> os= orderService.listByUserWithoutDelete(user);
        orderService.removeOrderFromOrderItem(os);
        return os;
    }

    @GetMapping("foreconfirmPay")
    public Object confirmPay(int oid) {
        Order o = orderService.get(oid);
        orderItemService.fill(o);
        orderService.cacl(o);
        orderService.removeOrderFromOrderItem(o);
        return o;
    }

    @GetMapping("foreorderConfirmed")
    public Object orderConfirmed( int oid) {
        Order o = orderService.get(oid);
        o.setStatus(OrderService.waitReview);
        o.setConfirmDate(new Date());
        orderService.update(o);
        return Result.success();
    }

    @PutMapping("foredeleteOrder")
    public Object deleteOrder(int oid){
        Order o = orderService.get(oid);
        o.setStatus(OrderService.delete);
        orderService.update(o);
        return Result.success();
    }

    @GetMapping("forereview")
    public Object review(int oid) {
        Order o = orderService.get(oid);
        orderItemService.fill(o);
        orderService.removeOrderFromOrderItem(o);
        Product p = o.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewService.list(p);
        productService.setSaleAndReviewNumber(p);
        Map<String,Object> map = new HashMap<>();
        map.put("p", p);
        map.put("o", o);
        map.put("reviews", reviews);

        return Result.success(map);
    }

    @PostMapping("foredoreview")
    public Object doreview( HttpSession session,int oid,int pid,String content) {
        Order o = orderService.get(oid);
        o.setStatus(OrderService.finish);
        orderService.update(o);

        Product p = productService.get(pid);
        content = HtmlUtils.htmlEscape(content);

        Users user =(Users)  session.getAttribute("user");
        Review review = new Review();
        review.setContent(content);
        review.setProduct(p);
        review.setCreateDate(new Date());
        review.setUser(user);
        reviewService.add(review);
        return Result.success();
    }

}
