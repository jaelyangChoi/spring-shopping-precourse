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
public class WishController {

    private final WishService wishService;

    /**
     * ToDO : @Login 애노테이션 만들어서 적용
     */
    @PostMapping
    public Wish addWish(@RequestBody Wish wish, @SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member loginMember) {
        log.info("login member: {}", loginMember);
        log.info("wish: {}", wish);
        wish.setMemberId(loginMember.getId());
        return wishService.save(wish);
    }


    @GetMapping
    public List<Wish> getWishes(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member loginMember) {
        log.info("login member: {}", loginMember);
        return wishService.findByMemberId(loginMember.getId());
    }

    @DeleteMapping("/{wishId}")
    public String deleteWish(@PathVariable Long wishId){
        wishService.delete(wishId);
        return "ok";
    }


}
