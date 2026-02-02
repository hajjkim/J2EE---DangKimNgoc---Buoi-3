package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "category/categories"; 
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("mode", "add");
        return "category/category-form"; 
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("category") Category category,
                      BindingResult result,
                      RedirectAttributes ra,
                      Model model) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "add");
            return "category/category-form"; 
        }
        categoryService.save(category);
        ra.addFlashAttribute("msg", " Thêm danh mục thành công!");
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Category c = categoryService.findById(id);
        if (c == null) {
            ra.addFlashAttribute("msg", " Không tìm thấy danh mục!");
            return "redirect:/categories";
        }
        model.addAttribute("category", c);
        model.addAttribute("mode", "edit");
        return "category/category-form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("category") Category category,
                       BindingResult result,
                       RedirectAttributes ra,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "edit");
            return "category/category-form"; 
        }
        category.setId(id);
        categoryService.save(category);
        ra.addFlashAttribute("msg", " Cập nhật danh mục thành công!");
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        if (!categoryService.existsById(id)) {
            ra.addFlashAttribute("msg", " Không tìm thấy danh mục!");
            return "redirect:/categories";
        }

        categoryService.deleteById(id);
        ra.addFlashAttribute("msg", " Đã xóa danh mục!");
        return "redirect:/categories";
    }
}
