package shopping.web;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import shopping.domain.Product;
import shopping.dto.ProductDto;
import shopping.repository.MemoryProductRepository;
import shopping.repository.ProductRepository;
import shopping.service.ProductService;

import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.*;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductValidationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private RestClient.Builder clientBuilder;
    private RestClient restClient;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    PlatformTransactionManager transactionManager; //스프링 부트는 자동으로 적절한 트랜잭션 매니저륿 빈으로 등록
    TransactionStatus status;


    @BeforeEach
    void setUp() {
        restClient = clientBuilder.build();

        productRepository.addProduct(new Product("product1", 1000, "imageUrl1"));
        productRepository.addProduct(new Product("product2", 2000, "imageUrl2"));

        //트랜잭션 시작
        status = transactionManager.getTransaction(new DefaultTransactionDefinition());
    }

    @AfterEach
    void tearDown() {
        if (productRepository instanceof MemoryProductRepository) {
            MemoryProductRepository memoryProductRepository = (MemoryProductRepository) productRepository;
            memoryProductRepository.clear();
        } else {
            entityManager.createNativeQuery("TRUNCATE TABLE PRODUCT").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE PRODUCT ALTER COLUMN ID RESTART WITH 1").executeUpdate();

            //트랜잭션 종료
            transactionManager.rollback(status);
        }
    }


    @Test
    @DisplayName("상품 이름이 15자를 넘을 경우 상품을 생성하면 400을 응답한다")
    void test1() {
        // Given 상품 이름이 "동해물과 백두산이 마르고 닳도록"일 때
        ProductDto productDto = new ProductDto("동해물과 백두산이 마르고 닳도록", 1000, "url://");

        // When 상품을 생성하면
        String url = "http://localhost:" + port + "/api/products";
        try {
            restClient.post()
                    .uri(url)
                    .body(productDto)
                    .retrieve()
                    .toEntity(String.class);
        } catch (RestClientResponseException e) {
            // 4xx 또는 5xx 오류 처리
            HttpStatusCode statusCode = e.getStatusCode();
            assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(e.getResponseBodyAsString()).isEqualTo("상품의 이름은 15자를 넘길 수 없습니다.");
        }
    }

    @Test
    @DisplayName("상품의 이름에 ( ), [ ], +, -, &, /, _ 외 특수 문자를 입력할 수 없습니다.")
    void test2() {
//        Given 상품 이름이 "아메리카노 *할인"일 때
        ProductDto productDto = new ProductDto("아메리카노 *할인", 1000, "url://");

//        When 상품을 생성하면
        String url = "http://localhost:" + port + "/api/products";

        try {
            restClient.post()
                    .uri(url)
                    .body(productDto)
                    .retrieve()
                    .toEntity(String.class);
        } catch (RestClientResponseException e) {
            // 4xx 또는 5xx 오류 처리
            HttpStatusCode statusCode = e.getStatusCode();
            assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(e.getResponseBodyAsString()).isEqualTo("상품의 이름에 ( ), [ ], +, -, &, /, _ 외 특수 문자를 입력할 수 없습니다.");

        }
    }

    @Test
    void 욕설_미포함(){
        final var request = new ProductDto("fuxx", 1000, "image");
        assertThatNoException()
                .isThrownBy(()->productService.save(request));
    }
}
