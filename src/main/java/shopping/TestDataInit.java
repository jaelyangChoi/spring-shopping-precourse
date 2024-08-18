package shopping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import shopping.domain.Product;
import shopping.repository.ProductRepository;

@Slf4j
@RequiredArgsConstructor
public class TestDataInit {
    private final ProductRepository productRepository;

    /**
     * 확인용 초기 데이터 추가
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("test data init");
        productRepository.save(new Product("Whiskey", 50000, "https://images.unsplash.com/photo-1716043657397-92666764b512?q=80&w=2614&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        productRepository.save(new Product("beer", 10000, "https://images.unsplash.com/photo-1634604536807-3dcaf9a6b688?q=80&w=2565&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));

    }
}
