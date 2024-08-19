package shopping.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import shopping.domain.Product;
import shopping.dto.ProductDto;
import shopping.repository.ProductRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
    private ProductController productController;

    @BeforeEach
    void setUp() {
        restClient = clientBuilder.build();

        productRepository.addProduct(new Product("product1", 1000, "imageUrl1"));
        productRepository.addProduct(new Product("product2", 2000, "imageUrl2"));
    }

    @Test
    void findProduct() {
        String url = "http://localhost:" + port + "/api/products/1";
        final Product findProduct = restClient.get()
                .uri(url)
                .retrieve()
                .body(Product.class);

        Product savedProduct = productRepository.getProduct(1L).orElse(null);
        assertThat(findProduct).isEqualTo(savedProduct);
    }

    @Test
    void findAllProduct() {
        String url = "http://localhost:" + port + "/api/products";
        final List<ProductDto> findProducts = restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){} );

        assertThat(findProducts).hasSize(2);
    }
//
//    @Test
//    void updateProduct() {
//    }
//
//    @Test
//    void deleteProduct() {
//    }
}
