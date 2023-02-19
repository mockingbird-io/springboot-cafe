package com.mockingbird.tmall.service;


import com.mockingbird.tmall.dao.UserDAO;
import com.mockingbird.tmall.pojo.Users;
import com.mockingbird.tmall.util.Page4Navigator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class UserService {
    @Resource
    UserDAO userDAO;

    public Page4Navigator<Users> list(int start, int size, int navigatePages) {
        Pageable pageable = PageRequest.of(start, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Users> pageFromJPA =userDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    public boolean isExist(String name){
        Users users = userDAO.findByName(name);
        return users != null;
    }

    public void add(Users users){
        userDAO.save(users);
    }
}
