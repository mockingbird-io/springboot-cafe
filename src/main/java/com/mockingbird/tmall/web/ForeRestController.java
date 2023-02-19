package com.mockingbird.tmall.web;

import com.mockingbird.tmall.pojo.Category;
import com.mockingbird.tmall.pojo.Users;
import com.mockingbird.tmall.service.CategoryService;
import com.mockingbird.tmall.service.ProductService;
import com.mockingbird.tmall.service.UserService;
import com.mockingbird.tmall.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class ForeRestController {
    @Resource
    CategoryService categoryService;
    @Resource
    ProductService productService;
    @Resource
    UserService userService;

    @GetMapping("/forehome")
    public Object home(){
        List<Category> list = categoryService.list();
        productService.fill(list);
        productService.fillByRow(list);
        categoryService.removeCategoryFromProduct(list);
        return list;
    }

    @PostMapping("/foreregister")
    public Object register(@RequestBody Users users){
        String name = users.getName();
        name = HtmlUtils.htmlEscape(name);
        users.setName(name);
        boolean exist = userService.isExist(name);
        if (exist){
            String message = "用户名已经被使用,不能使用";
            return Result.fail(message);
        }
        userService.add(users);
        return Result.success();
    }
}
