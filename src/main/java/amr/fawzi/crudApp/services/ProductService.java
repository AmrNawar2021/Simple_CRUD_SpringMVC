package amr.fawzi.crudApp.services;

import amr.fawzi.crudApp.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();
    Product getProductById(int id);
    void createProduct(Product product, MultipartFile imageFile);
    void updateProduct(int id, Product product, MultipartFile imageFile);
    void deleteProduct(int id);




}
