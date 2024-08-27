package shopping.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import shopping.domain.Product;
import shopping.domain.dto.ProductUpdateDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductAcceptanceTest {

    @LocalServerPort
    private int port;
    private String url;

    @Autowired
    private RestClient.Builder builder;
    private RestClient client;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        url = "http://localhost:" + port;
        client = builder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Test
    void 상품_생성_후_상품_목록에_포함됨() throws Exception {
        var request = new ProductUpdateDto("현대오토에버", 10_000, "image");
        var response = 상품_생성(request);
        상품이_생성됨(response);
        var responses = 상품_목록_조회();
        상품_목록에_포함됨(responses, request);
    }

    private ResponseEntity<Product> 상품_생성(ProductUpdateDto productUpdateDto) {
        return client
                .post()
                .uri(url + "/api/products")
                .body(productUpdateDto)
                .retrieve()
                .toEntity(Product.class);
    }

    private void 상품이_생성됨(ResponseEntity<Product> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getClass().equals(Product.class));
    }

    private ResponseEntity<List<Product>> 상품_목록_조회() {
        return client
                .get()
                .uri(url + "/api/products")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<Product>>() {
                });
    }

    private void 상품_목록에_포함됨(ResponseEntity<List<Product>> response, ProductUpdateDto requestDto) throws Exception {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertProduct(response.getBody().get(0), requestDto);
    }

    private void assertProduct(Product actual, ProductUpdateDto expected) {
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl());
    }
}
