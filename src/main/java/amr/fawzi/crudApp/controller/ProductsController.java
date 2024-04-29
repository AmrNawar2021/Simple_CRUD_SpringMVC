package amr.fawzi.crudApp.controller;

import amr.fawzi.crudApp.model.Product;
import amr.fawzi.crudApp.model.ProductDTO;
import amr.fawzi.crudApp.repository.ProductRepository;

import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping({"", "/"})
    public String showProductList(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "products/index";
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        ProductDTO productDTO = new ProductDTO();
        model.addAttribute("productDto", productDTO);
        return "products/createproduct";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("productDto") ProductDTO productDTO, BindingResult bindingResult) {

        if (productDTO.getImageFile().isEmpty()) {
            bindingResult.addError(new FieldError("productDto", "imageFile", "Image file is empty"));
        }
        if (bindingResult.hasErrors()) {
            return "products/createproduct";
        }

        MultipartFile image = productDTO.getImageFile();
        Date date = new Date();
        String storageFileName = date.getTime() + " " + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir, storageFileName), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setBrand(productDTO.getBrand());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());
        product.setDescription(productDTO.getDescription());
        product.setCreatedAt(date);
        product.setImageFileName(storageFileName);
        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditForm(Model model,@RequestParam int id) {


        try {
            Product product = productRepository.findById(id).get();
            model.addAttribute("product", product);


            ProductDTO productDTO  = new ProductDTO();
            productDTO.setName(product.getName());
            productDTO.setBrand(product.getBrand());
            productDTO.setPrice(product.getPrice());
            productDTO.setDescription(product.getDescription());

            model.addAttribute("productDto", productDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return "products/editProduct";
        }
        return "products/editProduct";
    }


    @PostMapping("/edit")
    public String updateProduct(Model model, @RequestParam int id, @Valid @ModelAttribute ProductDTO productDTO, BindingResult bindingResult) {

        try {
            Product product = productRepository.findById(id).get();
            model.addAttribute("product", product);
            if (bindingResult.hasErrors()){
                return "products/editProduct";
            }

            if (!productDTO.getImageFile().isEmpty()){
                String uploadDir = "public/images/";
                Path oldPath = Paths.get(uploadDir + product.getImageFileName());

                try {
                    Files.delete(oldPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                MultipartFile image = productDTO.getImageFile();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + " " + image.getOriginalFilename();
                try (InputStream inputStream = image.getInputStream()){
                    Files.copy(inputStream,Paths.get(uploadDir + storageFileName ), StandardCopyOption.REPLACE_EXISTING);
                }
                product.setImageFileName(storageFileName);
            }
            product.setName(productDTO.getName());
            product.setBrand(productDTO.getBrand());
            product.setPrice(productDTO.getPrice());
            product.setDescription(productDTO.getDescription());
            product.setCategory(productDTO.getCategory());
            productRepository.save(product);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return "redirect:/products";
    }


    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id) {

        try {


            Product product = productRepository.findById(id).get();

            Path path = Paths.get("public/images/" + product.getImageFileName());
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            productRepository.delete(product);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/products";
    }


}
