package com.prem.ecommerce.Service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.Producible;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.prem.ecommerce.ExceptionHandlers.ResourceNotFoundException;
import com.prem.ecommerce.Model.Category;
import com.prem.ecommerce.Model.Product;
import com.prem.ecommerce.Payload.CategoryDTO;
import com.prem.ecommerce.Payload.ProductDTO;
import com.prem.ecommerce.Payload.ProductResponse;
import com.prem.ecommerce.Repository.CategoryRepository;
import com.prem.ecommerce.Repository.ProductRepository;
import com.prem.ecommerce.Service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;


    public ProductDTO addProduct(Product product, Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
        .orElseThrow(()-> new ResourceNotFoundException("category", "categoryId" , categoryId));

        product.setCategory(category);

        product.setImage("default.png");
        Double specialPrice = product.getPrice() - (product.getDiscount()* 0.01)*product.getPrice(); 
        product.setSpecialPrice(specialPrice);
                
        Product savedProduct = productRepository.save(product);


        ProductDTO productDTO = modelMapper.map(savedProduct, ProductDTO.class);
        return productDTO;
    }


    @Override
    public ProductResponse getAllProducts() {
        
        List<Product> products = productRepository.findAll();
     
        List<ProductDTO> productDTOs = products.stream()
        .map(product -> modelMapper.map(product,ProductDTO.class))
        .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);
        return productResponse;
    }


    @Override
    public ProductResponse getAllProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
        .orElseThrow(()-> new ResourceNotFoundException("category","categoryId",categoryId));

        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);

        // Map the products to DTOs as a separate step.
    List<ProductDTO> productDTOs = products.stream()
        .map(product -> modelMapper.map(product, ProductDTO.class))
        .toList();

    // Create and populate the response object.
    ProductResponse productResponse = new ProductResponse();
    productResponse.setContent(productDTOs);

    return productResponse;
        

    }


    @Override
    public ProductResponse getAllProductsByKeyword(String keyWord) {
        // List<Product> products = productRepository.findAll();  //findByProductNameLikeIgnoreCase('%' + keyWord + '%')

        // List<ProductDTO> filteredProducts  = products.stream()
        // .filter(product -> product.getProductName().contains(keyWord))
        // .map(product -> modelMapper.map(product, ProductDTO.class))
        // .toList();

       List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyWord + '%');

        List<ProductDTO> filteredProducts  = products.stream()
        .map(product -> modelMapper.map(product, ProductDTO.class))
        .toList();


        ProductResponse productResponse = new ProductResponse();
    productResponse.setContent(filteredProducts);

    return productResponse;

    }

    

}
