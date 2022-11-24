package MockingBird.Springbootcafe.Service;

import MockingBird.Springbootcafe.Dao.CategoryDao;
import MockingBird.Springbootcafe.Pojo.Category;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    CategoryDao categoryDao;
    public List<Category> list(){
        return categoryDao.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }
}
