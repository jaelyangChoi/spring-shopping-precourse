package shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
}
