package com.prem.ecommerce.Controlller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prem.ecommerce.Model.Product;
import com.prem.ecommerce.Payload.ProductDTO;
import com.prem.ecommerce.Payload.ProductResponse;
import com.prem.ecommerce.Service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    ResponseEntity<ProductDTO> addProduct(@RequestBody Product product, @PathVariable Long categoryId)
    {
        return new ResponseEntity<>(productService.addProduct(product,categoryId),HttpStatus.CREATED) ;
    }

    @GetMapping("/public/products")

    ResponseEntity<ProductResponse> getAllProducts()
    {
         ProductResponse productResponse = productService.getAllProducts();
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }


     @GetMapping("/public/categories/{categoryId}/products")

    ResponseEntity<ProductResponse> getAllProductsByCategory(@PathVariable Long categoryId)
    {
         ProductResponse productResponse = productService.getAllProductsByCategory(categoryId);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }


}
