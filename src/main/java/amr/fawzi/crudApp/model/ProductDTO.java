package amr.fawzi.crudApp.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class ProductDTO {

    @NotEmpty(message = "Name Is Required")
    private String name;
    @Min(0)
    private double price;
    @NotEmpty(message = "Brand Is Required")
    private String brand;
    @NotEmpty(message = "Category Is Required")
    private String category;
    @Size(min = 10,message = "the description should be at least 10 characters")
    @Size(min = 10,message = "the description should not be more than 1000 characters")
    @Column(columnDefinition = "Text")
    private String description;
    private Date createdAt;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
