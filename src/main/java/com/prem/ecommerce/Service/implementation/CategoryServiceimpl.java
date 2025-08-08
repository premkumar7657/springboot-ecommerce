package com.prem.ecommerce.Service.implementation;

import java.util.*;

import org.springframework.stereotype.Service;
import com.prem.ecommerce.Model.Category;

import com.prem.ecommerce.Service.CategoryService;

@Service
public class CategoryServiceimpl implements CategoryService{

   List<Category> categoryData = new ArrayList<>();

    @Override
    public List<Category> getCategories() {
        // TODO Auto-generated method stub
        return categoryData;

        
    }

    @Override
    public String addCategory(Category category) {
        // TODO Auto-generated method stub
        categoryData.add(category);
        return "added successfully";
        
    }

}
