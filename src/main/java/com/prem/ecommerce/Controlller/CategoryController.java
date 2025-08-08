package com.prem.ecommerce.Controlller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.prem.ecommerce.Model.Category;
import com.prem.ecommerce.Service.CategoryService;

import java.util.*;

@RestController
public class CategoryController {

    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/public/category")
    public List<Category> getCategories()
    {
       return categoryService.getCategories();
    }

    @PostMapping("/api/public/category")
    public String addCategory(@RequestBody Category category)
    {
       return categoryService.addCategory(category);
    }

   
}



