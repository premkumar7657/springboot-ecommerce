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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.prem.ecommerce.Config.AppConstants;
import com.prem.ecommerce.Model.Category;
import com.prem.ecommerce.Payload.CategoryDTO;
import com.prem.ecommerce.Payload.CategoryResponse;
import com.prem.ecommerce.Service.CategoryService;

import ch.qos.logback.core.joran.spi.HttpUtil.RequestMethod;
import jakarta.validation.Valid;

import java.util.*;

@RestController
public class CategoryController {

    // As we have common RestControllerAdvice are there to handle the exception, We dont need to write the try,catch block for each time.
    

    CategoryService categoryService;



    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    // @GetMapping("api/public/getvaluefromurl")
    // public ResponseEntity<String> getValueFromURL(@RequestParam(name = "message")String message)
    // {
    //     return new ResponseEntity<>(message,HttpStatus.OK);
    // }


    @GetMapping("/api/public/category")
    public ResponseEntity<CategoryResponse> getCategories(
        @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false)Integer pageSize,
        @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER ,required = false)Integer pageNumber,
        @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY ,required = false) String sortBy,
        @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER ,required = false) String sortOrder)
    {

       CategoryResponse categories = categoryService.getCategories(pageSize,pageNumber,sortBy,sortOrder);
       return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @PostMapping("/api/public/category")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDto) //valid - data which we pass must be validated instead giving each variable to validate....also user friendly
    {
        CategoryDTO categoryDTO2 = categoryService.addCategory(categoryDto);
       return new ResponseEntity<>(categoryDTO2,HttpStatus.CREATED);
    }

    //  @RequestMapping(value = "/api/public/category" , method = RequestMethod.POST)
    // public ResponseEntity<String> addCategory(@RequestBody Category category)
    // {
    //     String status = categoryService.addCategory(category);
    //    return new ResponseEntity<>(status,HttpStatus.CREATED);
    // }


    @DeleteMapping("api/admin/categories/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id)
    {
        //try{
            CategoryDTO status = categoryService.deleteCategory(id);
        return new ResponseEntity<>(status,HttpStatus.OK);
        // }
        // catch(ResponseStatusException e)
        // {
        //     return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        // }
    }

    // public String deleteCategory(@PathVariable Long id)
    // {
    //     return categoryService.deleteCategory(id);
    // }

   


    @PutMapping("api/admin/categories/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDto,@PathVariable Long id)
    {
        //try{
                CategoryDTO data = categoryService.updateCategory(id,categoryDto);
                return new ResponseEntity<CategoryDTO>(data, HttpStatus.OK);
        // }

        // catch(ResponseStatusException e)
        // {
        //     return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        // }
      
      
    }

   
}



