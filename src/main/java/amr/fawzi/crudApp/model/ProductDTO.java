package amr.fawzi.crudApp.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class ProductDTO {

    public ProductDTO(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.brand = product.getBrand();
        this.category = product.getCategory();
        this.description = product.getDescription();
    }
    public ProductDTO() {}


    @NotEmpty(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    private double price;

    @NotEmpty(message = "Brand is required")
    private String brand;

    @NotEmpty(message = "Category is required")
    private String category;

    @NotEmpty(message = "Description is required")
    private String description;

    @NotNull(message = "Image file is required")
    private MultipartFile imageFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public Product toProduct() {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setBrand(brand);
        product.setCategory(category);
        product.setDescription(description);
        return product;
    }
}
