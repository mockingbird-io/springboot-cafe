package MockingBird.Springbootcafe.Service;

import MockingBird.Springbootcafe.Dao.CategoryDao;
import MockingBird.Springbootcafe.Pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryDao categoryDao;
    public List<Category> list(){
        return categoryDao.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }

    public void add(Category category){
        categoryDao.save(category);
    }
}
