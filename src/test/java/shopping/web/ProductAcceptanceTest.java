package shopping.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductAcceptanceTest {
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
        var request = new CreateProductRequest("현대오토에버", 10_000, "image");
        var response = 상품_생성(request);
        상품이_생성됨(response);
        var responses = 상품_목록_조회();
        상품_목록에_포함됨(responses, request);
    }

    private ResponseEntity<Void> 상품_생성(CreateProductRequest request) {
        return client
                .post()
                .uri(url + "/api/products")
                .body(request)
                .retrieve()
                .toEntity(Void.class);
    }

    private void 상품이_생성됨(ResponseEntity<Void> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation().toString()).contains("/api/products");
    }

    private ResponseEntity<String> 상품_목록_조회() {
        return client
                .get()
                .uri(url + "/api/products")
                .retrieve()
                .toEntity(String.class);
    }

    private void 상품_목록에_포함됨(ResponseEntity<String> response, CreateProductRequest request) throws Exception {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var actual = mapper.readValue(response.getBody(), ProductResponse[].class);
        assertProduct(actual[0], request);
    }

    private void assertProduct(ProductResponse actual, CreateProductRequest expected) {
        assertThat(actual.id()).isNotNull();
        assertThat(actual.name()).isEqualTo(expected.name());
        assertThat(actual.price()).isEqualTo(expected.price());
        assertThat(actual.imageUrl()).isEqualTo(expected.imageUrl());
    }

    record CreateProductRequest(String name, Integer price, String imageUrl){
    }

    record ProductResponse(Long id, String name, Integer price, String imageUrl){
    }
}