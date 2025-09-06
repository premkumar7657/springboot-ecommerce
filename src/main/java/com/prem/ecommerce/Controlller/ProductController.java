package com.prem.ecommerce.Controlller;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.MultipartFile;

import com.prem.ecommerce.Config.AppConstants;
import com.prem.ecommerce.Model.Product;
import com.prem.ecommerce.Payload.ProductDTO;
import com.prem.ecommerce.Payload.ProductResponse;
import com.prem.ecommerce.Service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDto, @PathVariable Long categoryId)
    {
        return new ResponseEntity<>(productService.addProduct(productDto,categoryId),HttpStatus.CREATED) ;
    }

    @GetMapping("/public/products")

    ResponseEntity<ProductResponse> getAllProducts(
        @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false)Integer pageSize,
        @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER ,required = false)Integer pageNumber,
        @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY_PRODUCT ,required = false) String sortBy,
        @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER ,required = false) String sortOrder)
    {
         ProductResponse productResponse = productService.getAllProducts(pageSize,pageNumber,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }


     @GetMapping("/public/categories/{categoryId}/products")

    ResponseEntity<ProductResponse> getAllProductsByCategory(@PathVariable Long categoryId)
    {
         ProductResponse productResponse = productService.getAllProductsByCategory(categoryId);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }


    @GetMapping("public/products/keyword/{keyWord}")
    ResponseEntity<ProductResponse> getAllProductsByKeyword(@PathVariable String keyWord)
    {
         ProductResponse productResponse = productService.getAllProductsByKeyword(keyWord);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }


    @PutMapping("/products/{productId}")
    ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductDTO productDto)
    {
        ProductDTO productDTO2 = productService.updateProduct(productId,productDto);
        return new ResponseEntity<>(productDTO2,HttpStatus.OK);
    }


    @DeleteMapping("/admin/products/{productId}")

    ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId)
    {
        ProductDTO productDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}/image")

    ResponseEntity<ProductDTO> updateProductImage (@PathVariable Long productId, @RequestParam MultipartFile image)  throws IOException
    {
        ProductDTO productDTO = productService.updateProductImage(productId, image);

        return new ResponseEntity<>(productDTO, HttpStatus.OK);

    }


}
