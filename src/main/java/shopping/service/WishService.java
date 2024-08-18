package shopping.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shopping.domain.Wish;
import shopping.repository.WishRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WishService {

    @Autowired
    private final WishRepository wishRepository;

    public Wish save(Wish wish) {
        return wishRepository.save(wish);
    }

    public List<Wish> findByMemberId(Long memberId) {
        return wishRepository.findByMemberId(memberId);
    }

    public void delete(Long wishId) {
        wishRepository.deleteById(wishId);
    }

}
