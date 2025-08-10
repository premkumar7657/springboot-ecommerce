package com.prem.ecommerce.Service;

import java.util.List;


import com.prem.ecommerce.Model.Category;

public interface CategoryService {
    

    public List<Category> getCategories();
    public String addCategory(Category category);
    public String deleteCategory(Long id);
    public Category updateCategory(Long id, Category category);

}
