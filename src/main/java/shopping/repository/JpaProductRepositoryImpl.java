package shopping.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shopping.domain.Product;
import shopping.dto.ProductDto;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Repository
public class JpaProductRepositoryImpl implements ProductRepository {

    private  SpringDataJpaProductRepository repository;

    @Autowired
    public JpaProductRepositoryImpl(SpringDataJpaProductRepository springDataJpaProductRepository) {
        this.repository = springDataJpaProductRepository;
    }

    @Override
    public void addProduct(Product product) {
        repository.save(product);
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @Override
    public void updateProduct(Long id, ProductDto productDto) {
        Product findProduct = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found. id = " + id.toString()));
        findProduct.setName(productDto.getName());
        findProduct.setPrice(productDto.getPrice());
        findProduct.setImageUrl(productDto.getImageUrl());
    }

    @Override
    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }

    public void commit() {
        repository.flush();
    }

}
