package shopping.repository;

import org.springframework.stereotype.Repository;
import shopping.domain.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemoryProductRepository implements ProductRepository {

    private Map<Long, Product> products = new HashMap<Long, Product>();

    @Override
    public void addProduct(Product product) {

    }

    @Override
    public Product getProduct(Long id) {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    @Override
    public void updateProduct(Long id, ProductUpdateDto productUpdateDto) {

    }

    @Override
    public void deleteProduct(Long id) {

    }
}
