package com.prem.ecommerce.Service.implementation;

import java.util.*;

import com.prem.ecommerce.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.server.ResponseStatusException;

import com.prem.ecommerce.Model.Category;

import com.prem.ecommerce.Service.CategoryService;



@Service
public class CategoryServiceimpl implements CategoryService{

    //Long id = 1l;

    @Autowired
    CategoryRepository categoryRepository;

  // List<Category> categoryData = new ArrayList<>();

    @Override
    public List<Category> getCategories() {
       //return categoryData;
        return categoryRepository.findAll();
    }

    @Override
    public String addCategory(Category category) {
        //category.setCategoryId(id++);
        //category.add(category);
        categoryRepository.save(category);
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

//        Category category = categoryData.stream()
//        .filter(c-> c.getCategoryId().equals(id))
//        .findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Category is not found"));
         Category category =categoryRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Category is not found"));
             categoryRepository.deleteById(id);
             return "Category deleted successfully for this " + id + " ";
         }


       // categoryData.remove(category);

       // return "Category deleted successfully for this " + id + " ";


     @Override
     public Category updateCategory(Long id, Category category) {
        // TODO Auto-generated method stub
//       Optional<Category> existingOptionalData = categoryData.stream()
//        .filter(c-> c.getCategoryId().equals(id))
//        .findFirst();
         Optional<Category> existingOptionalData = categoryRepository.findById(id);

        if(existingOptionalData.isPresent())
        {
            Category existingData = existingOptionalData.get();
            existingData.setCategoryName(category.getCategoryName());
            Category savedcategory = categoryRepository.save(existingData);
            return savedcategory;
        }

        else{
          throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category is not found "+ id);
        }

        
     }

}
