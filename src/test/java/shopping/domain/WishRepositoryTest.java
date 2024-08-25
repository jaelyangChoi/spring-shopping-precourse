package shopping.domain;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shopping.repository.WishRepository;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
public class WishRepositoryTest {

    @Autowired
    WishRepository wishRepository;

    @Test
    void save() {
        //given
        Wish wish = new Wish(1L, 1L);

        //when
        Wish savedWish = wishRepository.save(wish);

        //then
        Wish findWish = wishRepository.findById(savedWish.getId()).orElseThrow();
        assertThat(savedWish).isEqualTo(findWish);
    }

    @Test
    void findById() {
        //given
        Wish wish1 = new Wish(1L, 1L);
        Wish wish2 = new Wish(1L, 2L);

        //when
        wishRepository.save(wish1);
        wishRepository.save(wish2);

        //then
        assertThat(wishRepository.findByMemberId(wish1.getMemberId()).size()).isEqualTo(2);
    }
}
