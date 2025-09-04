

import com.prem.ecommerce.Model.Product;
import com.prem.ecommerce.Payload.ProductDTO;
import com.prem.ecommerce.Payload.ProductResponse;


public interface ProductService {

    public ProductDTO addProduct(Product product, Long categoryId);

    public ProductResponse getAllProducts();

    public ProductResponse getAllProductsByCategory(Long categoryId);


}
