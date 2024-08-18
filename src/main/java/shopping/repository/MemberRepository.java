package shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByEmailAndPassword(String email, String password);
}
