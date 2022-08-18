package Pojo;

import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.Resource;
public class Product {
    private int id;
    private String name;
    @Resource(name="c")
    private Category category;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
