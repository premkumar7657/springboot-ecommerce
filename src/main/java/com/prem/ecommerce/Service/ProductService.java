

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.prem.ecommerce.Model.Product;
import com.prem.ecommerce.Payload.ProductDTO;
import com.prem.ecommerce.Payload.ProductResponse;


public interface ProductService {

    public ProductDTO addProduct(ProductDTO productDto, Long categoryId);

    public ProductResponse getAllProducts(Integer pageSize,Integer pageNumber,String sortBy,String sortOrder);

    public ProductResponse getAllProductsByCategory(Long categoryId,Integer pageSize,Integer pageNumber,String sortBy,String sortOrder);

    public ProductResponse getAllProductsByKeyword(String keyWord,Integer pageSize,Integer pageNumber,String sortBy,String sortOrder);

    public ProductDTO updateProduct(Long productId, ProductDTO productDto);

    public ProductDTO deleteProduct(Long productId);

    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;


}
