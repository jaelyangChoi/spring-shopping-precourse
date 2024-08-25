package shopping.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shopping.domain.Member;
import shopping.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member register(Member member) {
        if (findByEmail(member.getEmail()).isPresent()) {
            throw new RuntimeException("registeredEmail");
        }
        memberRepository.save(member);
        return member;
    }

    public Optional<Member> login(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
