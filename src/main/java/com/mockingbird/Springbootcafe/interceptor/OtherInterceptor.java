package com.mockingbird.Springbootcafe.interceptor;

import com.mockingbird.Springbootcafe.pojo.Category;
import com.mockingbird.Springbootcafe.pojo.OrderItem;
import com.mockingbird.Springbootcafe.pojo.Users;
import com.mockingbird.Springbootcafe.service.CategoryService;
import com.mockingbird.Springbootcafe.service.OrderItemService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class OtherInterceptor implements HandlerInterceptor {
    @Resource
    CategoryService categoryService;
    @Resource
    OrderItemService orderItemService;
    @Resource
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
        HttpSession session = httpServletRequest.getSession();
        Users user =(Users) session.getAttribute("user");
        int  cartTotalItemNumber = 0;
        if(null!=user) {
            List<OrderItem> ois = orderItemService.listByUsers(user);
            for (OrderItem oi : ois) {
                cartTotalItemNumber+=oi.getNumber();
            }

        }

        List<Category> cs =categoryService.list();
        String contextPath=httpServletRequest.getServletContext().getContextPath();

        httpServletRequest.getServletContext().setAttribute("categories_below_search", cs);
        session.setAttribute("cartTotalItemNumber", cartTotalItemNumber);
        httpServletRequest.getServletContext().setAttribute("contextPath", contextPath);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
    }
}
