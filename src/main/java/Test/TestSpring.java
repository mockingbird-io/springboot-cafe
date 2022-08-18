package Test;

import Pojo.Product;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import Pojo.Category;
import Service.ProductService;

public class TestSpring {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {"applicationContext.xml"});

        ProductService s = (ProductService) context.getBean("s");
        s.doSomeService();
    }
}