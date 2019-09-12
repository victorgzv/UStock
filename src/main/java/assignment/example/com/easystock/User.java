package assignment.example.com.easystock;

/**
 * Created by Víctor on 17/11/2017.
 * User class:This class contains the required attributes, class constructor, getter and setter methods to handle users objects-
 */

public class User {
    String name;
    String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return this.name;
    }
}
