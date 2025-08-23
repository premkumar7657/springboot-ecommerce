package com.prem.ecommerce.Service;

import java.util.List;


import com.prem.ecommerce.Model.Category;
import com.prem.ecommerce.Payload.CategoryDTO;
import com.prem.ecommerce.Payload.CategoryResponse;

public interface CategoryService {
    

    public CategoryResponse getCategories();
    public CategoryDTO addCategory(CategoryDTO categoryDto);
    public String deleteCategory(Long id);
    public Category updateCategory(Long id, Category category);

}
