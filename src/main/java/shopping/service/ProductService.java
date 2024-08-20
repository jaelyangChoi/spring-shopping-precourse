package shopping.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import shopping.domain.Product;
import shopping.dto.ProductDto;
import shopping.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Product findProduct(Long productId) {
        return productRepository.getProduct(productId).orElseThrow();
    }

    public List<Product> findAllProducts() {
        return productRepository.getAllProducts();
    }

    public void updateProduct(Long productId, ProductDto updateParam) {
        productRepository.updateProduct(productId, updateParam);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteProduct(productId);
    }
}
