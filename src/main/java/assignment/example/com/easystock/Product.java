package assignment.example.com.easystock;

/**
 * Created by Víctor on 22/11/2017.
 * Product class:This class contains the required attributes, class constructor, getter and setter methods to handle products objects.
 */

public class Product {
    private Integer id;
    private String barcode,name,description;
    private Integer categoryID,supplierID;
    Float price;
    Integer stock,qtyRequired;

    public Product(Integer id, String barcode, String name, String description, Integer categoryID, Integer supplierID, Float price, Integer stock, Integer qtyRequired) {
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.description = description;
        this.categoryID = categoryID;
        this.supplierID = supplierID;
        this.price = price;
        this.stock = stock;
        this.qtyRequired = qtyRequired;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public Integer getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Integer supplierID) {
        this.supplierID = supplierID;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getQtyRequired() {
        return qtyRequired;
    }

    public void setQtyRequired(Integer qtyRequired) {
        this.qtyRequired = qtyRequired;
    }
}
