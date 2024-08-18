package shopping.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shopping.domain.Member;
import shopping.domain.Wish;
import shopping.service.WishService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/wishes")
@RestController
public class wishController {

    private final WishService wishService;

    @PostMapping
    public Wish addWish(@RequestBody Wish wish, @SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member loginMember) {
        log.info("login member: {}", loginMember);
        log.info("wish: {}", wish);
        wish.setMemberId(loginMember.getMemberId());
        return wishService.save(wish);
    }


    @GetMapping
    public List<Wish> getWishes(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member loginMember) {
        log.info("login member: {}", loginMember);
        return wishService.findByMemberId(loginMember.getMemberId());
    }

    @DeleteMapping("/{wishId}")
    public String deleteWish(@PathVariable Long wishId){
        wishService.delete(wishId);
        return "ok";
    }


}
