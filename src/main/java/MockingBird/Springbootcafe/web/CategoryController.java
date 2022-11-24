package MockingBird.Springbootcafe.web;

import MockingBird.Springbootcafe.Dao.CategoryDao;
import MockingBird.Springbootcafe.Pojo.Category;
import MockingBird.Springbootcafe.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> list() throws Exception {
        return categoryService.list();
    }

    @GetMapping("/add")
    public void add() {
        Category bean = new Category();
        bean.setName("lazy11");
        categoryService.add(bean);
    }
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hellosss %s!", name);
    }
}
