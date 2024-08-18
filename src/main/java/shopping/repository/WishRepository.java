package shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.domain.Wish;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByMemberId(Long memberId);
}
