package com.prem.ecommerce.Repository;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.prem.ecommerce.Model.Category;
import com.prem.ecommerce.Model.Product;


public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findByCategoryOrderByPriceAsc(Category category,Pageable pageable);

    Page<Product> findByProductNameLikeIgnoreCase(String keyWord,Pageable pageable);


}
