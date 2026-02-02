package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }
    private String saveImage(MultipartFile file) {
    try {
        if (file == null || file.isEmpty()) return null;

        String uploadDir = "uploads/";
        Files.createDirectories(Paths.get(uploadDir));

        String original = file.getOriginalFilename();
        String safeName = (original == null) ? "image" : original.replaceAll("\\s+", "_");
        String fileName = UUID.randomUUID() + "_" + safeName;

        Path filePath = Paths.get(uploadDir).resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    } catch (Exception e) {
        throw new RuntimeException("Lỗi upload ảnh: " + e.getMessage(), e);
    }
}


    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "product/products";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("mode", "add");
        return "product/product-form";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("product") Product product,
                      BindingResult result,
                      @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
                      RedirectAttributes ra,
                      Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("mode", "add");
            return "product/product-form";
        }

        String imageName = saveImage(imageFile);
        product.setImage(imageName);

        productService.save(product);
        ra.addFlashAttribute("msg", " Thêm sản phẩm thành công!");
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Product p = productService.findById(id);
        if (p == null) {
            ra.addFlashAttribute("msg", " Không tìm thấy sản phẩm!");
            return "redirect:/products";
        }

        model.addAttribute("product", p);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("mode", "edit");
        return "product/product-form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("product") Product product,
                       BindingResult result,
                       @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
                       RedirectAttributes ra,
                       Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("mode", "edit");
            return "product/product-form";
        }

        Product old = productService.findById(id);
        if (old == null) {
            ra.addFlashAttribute("msg", " Không tìm thấy sản phẩm!");
            return "redirect:/products";
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageName = saveImage(imageFile);
            product.setImage(imageName);
        } else {
            product.setImage(old.getImage());
        }

        product.setId(id);
        productService.save(product);

        ra.addFlashAttribute("msg", " Cập nhật sản phẩm thành công!");
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        if (!productService.existsById(id)) {
            ra.addFlashAttribute("msg", " Không tìm thấy sản phẩm!");
            return "redirect:/products";
        }
        productService.deleteById(id);
        ra.addFlashAttribute("msg", " Đã xóa sản phẩm!");
        return "redirect:/products";
    }
}
