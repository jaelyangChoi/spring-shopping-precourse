package shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.domain.Product;

public interface SpringDataJpaProductRepository extends JpaRepository<Product, Long>  {
}
