package shopping.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import shopping.domain.Product;
import shopping.repository.ProductRepository;

import java.net.URI;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplateBuilder builder;
    private RestTemplate client;

    @Autowired
    private RestClient.Builder clientBuilder;
    private RestClient restClient;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        client = builder.build();
        restClient = clientBuilder.build();
    }

    //    private RestClient client;
//    private WebTestClient client;

    @Test
    void findByIdTest() {
        String url = "http://localhost:" + port + "/api/products/1";
        final Product actual = client.getForObject(url, Product.class);
        Product expected = productRepository.findById(1L).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void test1(){
        String url = "http://localhost:" + port + "/api/products";
        String actual = restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);

    }

    @Test
    void test3(){
        String url = "http://localhost:" + port + "/api/products";

        var request = new RequestEntity<>(
                new TestRequest("jaeryang"),
                HttpMethod.POST,
                URI.create(url)
        );

        var actual = client.exchange(request, String.class).getBody();
        assertThat(actual).isEqualTo("jaeryang");
    }
}
