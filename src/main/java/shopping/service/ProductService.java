package shopping.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shopping.domain.Product;
import shopping.repository.ProductRepository;
import shopping.domain.dto.ProductUpdateDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product save(ProductUpdateDto productDto) {
        return productRepository.save(productDto.toEntity());
    }

    public void update(Long productId, ProductUpdateDto updateParam) {
        Product findProduct = productRepository.findById(productId).orElseThrow();
        findProduct.setName(updateParam.getProductName());
        findProduct.setPrice(updateParam.getPrice());
        findProduct.setImageUrl(updateParam.getImageUrl());
    }

    public void delete(Long productId) {
        productRepository.deleteById(productId);
    }
}
