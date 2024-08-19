package shopping.repository;

import shopping.domain.Product;
import shopping.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    public void addProduct(Product product);
    public Optional<Product> getProduct(Long id);
    public List<Product> getAllProducts();
    public void updateProduct(Long id,  ProductDto productDto);
    public void deleteProduct(Long id);
}
