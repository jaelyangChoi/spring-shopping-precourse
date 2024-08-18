package shopping.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shopping.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void register() {
        //given
        Member member = new Member("cjl2076@naver.com", "0000", "cjl0701");

        //when
        memberRepository.save(member);

        //then
        Member findMember = memberRepository.findById(member.getEmail()).orElseThrow();
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void login() {
        //given
        Member member = new Member("cjl2076@naver.com", "0000", "cjl0701");
        memberRepository.save(member);

        //when
        Optional<Member> successMember = memberRepository.findByEmailAndPassword(member.getEmail(), member.getPassword());
        Optional<Member> failMember = memberRepository.findByEmailAndPassword(member.getEmail(), "");

        //then
        assertThat(successMember).isPresent();
        assertThat(failMember).isEmpty();
    }
}
