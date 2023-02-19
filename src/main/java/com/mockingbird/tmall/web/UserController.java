package com.mockingbird.tmall.web;

import com.mockingbird.tmall.pojo.Users;
import com.mockingbird.tmall.service.UserService;
import com.mockingbird.tmall.util.Page4Navigator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {
    @Resource
    UserService userService;

    @GetMapping("/users")
    public Page4Navigator<Users> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start<0?0:start;
        return userService.list(start,size,5);
    }
}
