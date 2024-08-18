package shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.domain.Wish;

public interface WishRepository extends JpaRepository<Wish, Long> {
}
