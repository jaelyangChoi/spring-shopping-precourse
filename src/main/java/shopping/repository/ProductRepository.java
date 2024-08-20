package shopping.repository;

import shopping.domain.Product;
import shopping.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void addProduct(Product product);
    Optional<Product> getProduct(Long id);
    List<Product> getAllProducts();
    void updateProduct(Long id,  ProductDto productDto);
    void deleteProduct(Long id);
}
