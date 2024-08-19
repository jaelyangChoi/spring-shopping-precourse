package shopping.repository;

import org.springframework.stereotype.Repository;
import shopping.domain.Product;
import shopping.dto.ProductDto;

import java.util.*;

@Repository
public class MemoryProductRepository implements ProductRepository {

    private Map<Long, Product> products = new HashMap<Long, Product>();
    private Long sequence = 0L;

    @Override
    public void addProduct(Product product) {
        products.put(++sequence, product);
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    @Override
    public void updateProduct(Long id, ProductDto productDto) {
        Product findProduct = products.get(id);
        findProduct.setName(productDto.getName());
        findProduct.setPrice(productDto.getPrice());
        findProduct.setImageUrl(productDto.getImageUrl());
    }

    @Override
    public void deleteProduct(Long id) {
        products.remove(id);
    }

    public void clear(){
        products.clear();
        sequence = 0L;
    }

}
