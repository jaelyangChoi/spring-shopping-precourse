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
        if (memberRepository.findById(member.getEmail()).isPresent()) {
            throw new RuntimeException("이미 가입된 회원 이메일입니다.");
        }
        memberRepository.save(member);
        return member;
    }

    public Optional<Member> login(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password);
    }
}
