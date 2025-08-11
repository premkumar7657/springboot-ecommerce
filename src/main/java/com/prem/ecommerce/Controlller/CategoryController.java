package com.prem.ecommerce.Controlller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.prem.ecommerce.Model.Category;
import com.prem.ecommerce.Service.CategoryService;

import ch.qos.logback.core.joran.spi.HttpUtil.RequestMethod;

import java.util.*;

@RestController
public class CategoryController {

    

    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/public/category")
    public ResponseEntity<List<Category>> getCategories()
    {
       List<Category> categories = categoryService.getCategories();
       return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @PostMapping("/api/public/category")
    public ResponseEntity<String> addCategory(@RequestBody Category category)
    {
        String status = categoryService.addCategory(category);
       return new ResponseEntity<>(status,HttpStatus.CREATED);
    }

    //  @RequestMapping(value = "/api/public/category" , method = RequestMethod.POST)
    // public ResponseEntity<String> addCategory(@RequestBody Category category)
    // {
    //     String status = categoryService.addCategory(category);
    //    return new ResponseEntity<>(status,HttpStatus.CREATED);
    // }


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

   


    @PutMapping("api/admin/categories/{id}")
    public ResponseEntity<?> updateCategory(@RequestBody Category category,@PathVariable Long id)
    {
        try{
                Category data = categoryService.updateCategory(id,category);
                return new ResponseEntity<>(data, HttpStatus.OK);
        }

        catch(ResponseStatusException e)
        {
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
      
      
    }

   
}



