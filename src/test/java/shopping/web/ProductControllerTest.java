package shopping.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.client.RestClient;
import shopping.domain.Product;
import shopping.dto.ProductDto;
import shopping.repository.JpaProductRepositoryImpl;
import shopping.repository.MemoryProductRepository;
import shopping.repository.ProductRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestClient.Builder clientBuilder;
    private RestClient restClient;

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
    void findProduct() {
        String url = "http://localhost:" + port + "/api/products/1";
        final ProductDto findProduct = restClient.get()
                .uri(url)
                .retrieve()
                .body(ProductDto.class);

        Product savedProduct = productRepository.getProduct(1L).orElseThrow();
        assertThat(findProduct).isEqualTo(ProductDto.fromEntity(savedProduct));
    }

    @Test
    void findAllProduct() {
        String url = "http://localhost:" + port + "/api/products";
        final List<ProductDto> findProducts = restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        assertThat(findProducts).hasSize(2);
    }

    @Test
    void updateProduct() {
        Long productId = 1L;
        String url = "http://localhost:" + port + "/api/products/" + productId;

        ProductDto updateParam = new ProductDto("productX", 1, "imageUrlX");
        String json;
        try {
            json = new ObjectMapper().writeValueAsString(updateParam);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String result = restClient.put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .body(String.class);

        assertThat(result).isEqualTo("success");
        Product updatedProduct = productRepository.getProduct(productId).orElseThrow();
        assertThat(updatedProduct.getName()).isEqualTo(updateParam.getName());
        assertThat(updatedProduct.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(updatedProduct.getImageUrl()).isEqualTo(updateParam.getImageUrl());
        org.junit.jupiter.api.Assertions.assertAll(
                () -> assertThat(updatedProduct.getName()).isEqualTo(updateParam.getName()),
                () -> assertThat(updatedProduct.getPrice()).isEqualTo(updateParam.getPrice()),
                () -> assertThat(updatedProduct.getImageUrl()).isEqualTo(updateParam.getImageUrl())
        );
    }

    @Test
    void updateProductNoSuchEx() {
        Long productId = 1000L;
        String url = "http://localhost:" + port + "/api/products/" + productId;

        ProductDto updateParam = new ProductDto("productX", 1, "imageUrlX");
        String json;
        try {
            json = new ObjectMapper().writeValueAsString(updateParam);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String result = restClient.put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .body(String.class);

        assertThat(result).isEqualTo("Product not found");
    }

    @Test
    void deleteProduct() {
        Long productId = 1L;
        String url = "http://localhost:" + port + "/api/products/" + productId;

        String result = restClient.delete()
                .uri(url)
                .retrieve()
                .body(String.class);

        assertThat(result).isEqualTo("success");
    }
}
