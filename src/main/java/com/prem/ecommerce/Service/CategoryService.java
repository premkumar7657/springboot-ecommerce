package com.prem.ecommerce.Service;

import java.util.List;


import com.prem.ecommerce.Model.Category;
import com.prem.ecommerce.Payload.CategoryDTO;
import com.prem.ecommerce.Payload.CategoryResponse;

public interface CategoryService {
    

    public CategoryResponse getCategories(Integer pageSize, Integer pageNumber,String sortBy,String sortOrder);
    public CategoryDTO addCategory(CategoryDTO categoryDto);
    public CategoryDTO deleteCategory(Long id);
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDto);

}
