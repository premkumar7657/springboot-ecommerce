package com.prem.ecommerce.Service.implementation;

import java.util.*;

import com.prem.ecommerce.Repository.CategoryRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.server.ResponseStatusException;

import com.prem.ecommerce.ExceptionHandlers.APIException;
import com.prem.ecommerce.ExceptionHandlers.ResourceNotFoundException;
import com.prem.ecommerce.Model.Category;
import com.prem.ecommerce.Payload.CategoryDTO;
import com.prem.ecommerce.Payload.CategoryResponse;
import com.prem.ecommerce.Service.CategoryService;



@Service
public class CategoryServiceimpl implements CategoryService{

    //Long id = 1l;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

  // List<Category> categoryData = new ArrayList<>();

    @Override
    public CategoryResponse getCategories() {
       //return categoryData;
        //return categoryRepository.findAll();

        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty())
         throw new APIException("There is no Categories added so far..!");

        List<CategoryDTO> categoryDTOS = categories.stream()
                            .map(category-> modelMapper.map(category, CategoryDTO.class))
                            .toList();

        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setContent(categoryDTOS);


         return categoryResponse;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDto) {

      // receiving data as categoryDTO

      Category category = modelMapper.map(categoryDto,Category.class);


      // validating any duplicate category name is available or not when we create a category

      Category savedCategoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());

      if(savedCategoryFromDB!=null)
        throw new APIException("Category with the name " + category.getCategoryName() + " is already exists!!!");

        //category.setCategoryId(id++);
        //category.add(category);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryDTO.class);
        
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
          //throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category is not found "+ id);
          throw new ResourceNotFoundException("Category","CategoryId",id);
        }

        
     }

}
