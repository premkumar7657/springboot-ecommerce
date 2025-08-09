package com.prem.ecommerce.Service.implementation;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.server.ResponseStatusException;

import com.prem.ecommerce.Model.Category;

import com.prem.ecommerce.Service.CategoryService;



@Service
public class CategoryServiceimpl implements CategoryService{

    Long id = 1l;

   List<Category> categoryData = new ArrayList<>();

    @Override
    public List<Category> getCategories() {
       
        return categoryData;

        
    }

    @Override
    public String addCategory(Category category) {
       
        category.setCategoryId(id++);
        categoryData.add(category);
        return "added successfully";
        
    }

    // @Override
    // public String deleteCategory(Long id) {

    //     Category category = categoryData.stream()
    //     .filter(c-> c.getCategoryId().equals(id))
    //     .findFirst().orElse(null);

    //     if(category==null)
    //     {
    //         return "category is not found";
    //     }

    //     categoryData.remove(category);

    //     return "Category deleted successfully for this " + id + " ";
    // }


     @Override
    public String deleteCategory(Long id) {

        Category category = categoryData.stream()
        .filter(c-> c.getCategoryId().equals(id))
        .findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Category is not found"));

        categoryData.remove(category);

        return "Category deleted successfully for this " + id + " ";
    }

}
