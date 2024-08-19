package shopping.service;

import org.springframework.stereotype.Service;
import shopping.domain.Product;
import shopping.dto.ProductDto;
import shopping.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Product findProduct(Long productId) {
        return productRepository.getProduct(productId).orElseThrow();
    }

    public List<Product> findAllProducts() {
        return productRepository.getAllProducts();
    }
}
