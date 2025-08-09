package com.prem.ecommerce.Controlller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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


    @DeleteMapping("api/admin/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id)
    {
        try{
            String status = categoryService.deleteCategory(id);
        return new ResponseEntity<>(status,HttpStatus.OK);
        }
        catch(ResponseStatusException e)
        {
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
    }

    // public String deleteCategory(@PathVariable Long id)
    // {
    //     return categoryService.deleteCategory(id);
    // }

   
}



