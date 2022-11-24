package MockingBird.Springbootcafe.Dao;

import MockingBird.Springbootcafe.Pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CategoryDao extends JpaRepository<Category,Integer> {
}
