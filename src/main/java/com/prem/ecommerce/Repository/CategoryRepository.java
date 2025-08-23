package com.prem.ecommerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prem.ecommerce.Model.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Category findByCategoryName(String categoryName);

}
