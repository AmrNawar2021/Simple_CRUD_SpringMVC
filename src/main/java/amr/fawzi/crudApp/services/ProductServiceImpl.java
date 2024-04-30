package amr.fawzi.crudApp.services;

import amr.fawzi.crudApp.model.Product;
import amr.fawzi.crudApp.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void createProduct(Product product, MultipartFile imageFile) {
        Date date = new Date();
        String storageFileName = saveImageFile(imageFile);
        product.setCreatedAt(date);
        product.setImageFileName(storageFileName);
        productRepository.save(product);
    }

    @Override
    public void updateProduct(int id, Product product, MultipartFile imageFile) {
        Product existingProduct = getProductById(id);
        if (existingProduct != null) {
            existingProduct.setName(product.getName());
            existingProduct.setBrand(product.getBrand());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setDescription(product.getDescription());

            if (!imageFile.isEmpty()) {
                String storageFileName = saveImageFile(imageFile);
                existingProduct.setImageFileName(storageFileName);
            }

            productRepository.save(existingProduct);
        }
    }

    @Override
    public void deleteProduct(int id) {
        Product product = getProductById(id);
        if (product != null) {
            deleteImageFile(product.getImageFileName());
            productRepository.delete(product);
        }
    }

    private String saveImageFile(MultipartFile imageFile) {
        try {
            Date date = new Date();
            String storageFileName = date.getTime() + " " + imageFile.getOriginalFilename();
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (var inputStream = imageFile.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir, storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }

            return storageFileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image file", e);
        }
    }

    private void deleteImageFile(String fileName) {
        String uploadDir = "public/images/";
        Path filePath = Paths.get(uploadDir + fileName);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image file", e);
        }
    }
}
