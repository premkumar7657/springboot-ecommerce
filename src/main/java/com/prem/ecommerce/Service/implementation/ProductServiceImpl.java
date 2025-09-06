package com.prem.ecommerce.Service.implementation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.prem.ecommerce.ExceptionHandlers.APIException;
import com.prem.ecommerce.ExceptionHandlers.ResourceNotFoundException;
import com.prem.ecommerce.Model.Category;
import com.prem.ecommerce.Model.Product;
import com.prem.ecommerce.Payload.CategoryDTO;
import com.prem.ecommerce.Payload.ProductDTO;
import com.prem.ecommerce.Payload.ProductResponse;
import com.prem.ecommerce.Repository.CategoryRepository;
import com.prem.ecommerce.Repository.ProductRepository;
import com.prem.ecommerce.Service.FileService;
import com.prem.ecommerce.Service.ProductService;


@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;


    public ProductDTO addProduct(ProductDTO productDto, Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
        .orElseThrow(()-> new ResourceNotFoundException("category", "categoryId" , categoryId));


        List<Product> products = category.getProducts();
        boolean isProductPresent = false;

        for(Product product: products )
        {
            if(product.getProductName().equals(productDto.getProductName()))
            {
                isProductPresent = true;
                break;
            } 
        }

        if(!isProductPresent)
        {
        Product product = modelMapper.map(productDto,Product.class);

        product.setCategory(category);
        product.setImage("default.png");

        Double specialPrice = product.getPrice() - (product.getDiscount()* 0.01)*product.getPrice(); 
        product.setSpecialPrice(specialPrice);
                
        Product savedProduct = productRepository.save(product);


        ProductDTO productDTO = modelMapper.map(savedProduct, ProductDTO.class);
        return productDTO;
        }

        else
        {
            throw new APIException("product already exists!");
        }
    }


    @Override
    public ProductResponse getAllProducts(Integer pageSize,Integer pageNumber,String sortBy,String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);

        Page<Product> pageProduct = productRepository.findAll(pageable);
        
        List<Product> products = pageProduct.getContent();

        if(products.isEmpty())
            throw new APIException("No products are found!!");

     
        List<ProductDTO> productDTOs = products.stream()
        .map(product -> modelMapper.map(product,ProductDTO.class))
        .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);

        productResponse.setPageNumber(pageProduct.getNumber());
        productResponse.setPageSize(pageProduct.getSize());
        productResponse.setTotalElements(pageProduct.getTotalElements());
        productResponse.setTotalPages(pageProduct.getTotalPages());
        productResponse.setLastPage(pageProduct.isLast());

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


    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDto) {
        
        Product existingProduct = productRepository.findById(productId)
        .orElseThrow(()-> new ResourceNotFoundException("product", "productId", productId));

        Product product = modelMapper.map(productDto, Product.class);

        existingProduct.setProductName(product.getProductName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setDiscount(product.getDiscount());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
    

        Double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice(); 
        existingProduct.setSpecialPrice(specialPrice);
    

        Product savedProduct = productRepository.save(existingProduct);

        ProductDTO productDTO = modelMapper.map(savedProduct, ProductDTO.class);

    return productDTO;


    }


    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
        .orElseThrow(()-> new ResourceNotFoundException("product","productID",productId));

        productRepository.delete(product);

        return modelMapper.map(product, ProductDTO.class);
    }


    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        
         Product product = productRepository.findById(productId)
        .orElseThrow(()-> new ResourceNotFoundException("product","productID",productId));

        //get the file name from the image and upload it in the project image path or server
        //String path = "images/"; move this property to application.properties
        String fileName = fileService.uploadImage(path, image);

        //updating the file name to the product
        product.setImage(fileName);

        //save the updated product 
        Product updateProduct = productRepository.save(product);

        return modelMapper.map(updateProduct, ProductDTO.class);

    }

    


    

    

}
