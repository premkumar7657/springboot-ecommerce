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
import org.springframework.boot.actuate.endpoint.Producible;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

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


    public ProductDTO addProduct(ProductDTO productDto, Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
        .orElseThrow(()-> new ResourceNotFoundException("category", "categoryId" , categoryId));

        Product product = modelMapper.map(productDto,Product.class);

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
        String path = "images/";
        String fileName = uploadImage(path, image);

        //updating the file name to the product
        product.setImage(fileName);

        //save the updated product 
        Product updateProduct = productRepository.save(product);

        return modelMapper.map(updateProduct, ProductDTO.class);

    }


    private String uploadImage(String path, MultipartFile image) throws IOException {
        
        // file names of current file or original file
        String originalName = image.getOriginalFilename();

        // generate a unique file name to avoid the overridden using UID
        String randomId = UUID.randomUUID().toString();

        //originalfilename animal.png --> 1dksjdhskd1213k313j.png
        String newFileName = randomId.concat(originalName.substring(originalName.lastIndexOf('.')));

        //creating original path
        String fileAbsPath = path + File.separator + newFileName;  // File.seperator = "/"

        //Check if the path is exist or create a new folder

        File folder = new File(path);
        if(!folder.exists())
        folder.mkdir();

        Files.copy(image.getInputStream(),Paths.get(fileAbsPath));

        return newFileName;

        



        //upload to the server 

        //returning file name
    }

    

}
