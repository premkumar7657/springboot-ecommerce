package com.prem.ecommerce.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prem.ecommerce.Model.Category;
import com.prem.ecommerce.Model.Product;


public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByCategoryOrderByPriceAsc(Category category);

    List<Product> findByProductNameLikeIgnoreCase(String keyWord);

}
