package shopping.web;

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
        var request = new ProductUpdateDto("상품", 10_000, "image");
        var response = 상품_생성(request);
        상품이_생성됨(response);
        var responses = 상품_목록_조회();
        상품_목록에_포함됨(responses, request);
    }

    @Test
    void 상품_업데이트() {
        var response = 상품_생성(new ProductUpdateDto("상품1", 10_000, "imageUrl1"));
        var productId = response.getBody().getId();

        var requestParam = new ProductUpdateDto("상품2", 10_000, "imageUrl1");
        상품_변경(productId ,requestParam);
        //조회하여 같은지 확인
        var updatedProduct = 상품_조회(productId).getBody();
        상품이_같은지_검증(updatedProduct, requestParam);
    }

    private void 상품이_같은지_검증(Product updatedProduct, ProductUpdateDto requestParam) {
        assertThat(updatedProduct.getName().equals(requestParam.getName()));
        assertThat(updatedProduct.getPrice().equals(requestParam.getPrice()));
        assertThat(updatedProduct.getImageUrl().equals(requestParam.getImageUrl()));
    }

    private ResponseEntity<Product> 상품_조회(Long productId) {
        return client
                .get()
                .uri(url + "/api/products/" + productId)
                .retrieve()
                .toEntity(Product.class);
    }

    private void 상품_변경(Long productId, ProductUpdateDto productUpdateDto) {
        client
                .post()
                .uri(url + "/api/products/" + productId)
                .body(productUpdateDto)
                .retrieve();
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