package assignment.example.com.easystock;

/**
 * Created by Víctor on 24/11/2017.
 * Category class:This class contains the required attributes, class constructor, getter and setter methods to handle categories objects-
 */

public class Category {
    Integer id;
    String name;

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
