package amr.fawzi.crudApp.controller;

import amr.fawzi.crudApp.model.Product;
import amr.fawzi.crudApp.model.ProductDTO;
import amr.fawzi.crudApp.repository.ProductRepository;

import amr.fawzi.crudApp.services.ProductService;
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
    private ProductService productService;

    @GetMapping({"", "/"})
    public String showProductList(Model model) {
        List<Product> products = productService.getAllProducts();
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
    public String createProduct(@Valid @ModelAttribute("productDto") ProductDTO productDTO, BindingResult bindingResult,
                                @RequestParam("imageFile") MultipartFile imageFile) {

        if (bindingResult.hasErrors()) {
            return "products/createproduct";
        }

        productService.createProduct(productDTO.toProduct(), imageFile);
        return "redirect:/products";
    }



    @GetMapping("/edit")
    public String showEditForm(Model model, @RequestParam int id) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        if (product == null) {
            // Handle product not found error
            return "redirect:/products";
        }

        ProductDTO productDTO = new ProductDTO(product);
        model.addAttribute("productDto", productDTO);
        return "products/editProduct";
    }


    @PostMapping("/edit")
    public String updateProduct(Model model, @RequestParam int id, @Valid @ModelAttribute ProductDTO productDTO,
                                BindingResult bindingResult, @RequestParam("imageFile") MultipartFile imageFile) {

        if (bindingResult.hasErrors()) {
            return "products/editProduct";
        }

        productService.updateProduct(id, productDTO.toProduct(), imageFile);
        return "redirect:/products";
    }


    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }


}
