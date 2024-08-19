package shopping.repository;

import org.springframework.stereotype.Repository;
import shopping.domain.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ProductRepository {
    public void addProduct(Product product);
    public Product getProduct(Long id);
    public List<Product> getAllProducts();
    public void updateProduct(Long id,  ProductUpdateDto productUpdateDto);
    public void deleteProduct(Long id);
}
