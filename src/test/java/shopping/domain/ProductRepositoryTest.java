package shopping.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shopping.repository.ProductRepository;
import shopping.domain.dto.ProductUpdateDto;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void save() {
        log.info("productRepository = {}", productRepository);
        //given
        Product product = new Product("productA", 10000, "https://plus.unsplash.com/premium_photo1");

        //when
        Product savedProduct = productRepository.save(product);

        //then
        Product findProduct = productRepository.findById(savedProduct.getId()).orElseThrow();
        assertThat(findProduct).isEqualTo(savedProduct);
    }

    @Test
    @DisplayName("객체에 set만해줘도 update가 적용된다.")
    void updateProduct() {
        //given
        Product product = new Product("productA", 10000, "https://plus.unsplash.com/premium_photo1");
        Long productId = productRepository.save(product).getId();

        //when
        ProductUpdateDto updateParam = new ProductUpdateDto("productB", 20000, "https://plus.unsplash.com/premium_photo2");
        product.setName(updateParam.getProductName());
        product.setPrice(updateParam.getPrice());
        product.setImageUrl(updateParam.getImageUrl());

        //then
        Product findProduct = productRepository.findById(productId).orElseThrow();
        assertThat(findProduct.getName()).isEqualTo(updateParam.getProductName());
        assertThat(findProduct.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(findProduct.getImageUrl()).isEqualTo(updateParam.getImageUrl());
    }

    @Test
    void findAll() {
        //given
        Product productA = new Product("productA", 10000, "https://plus.unsplash.com/premium_photo1");
        Product productB = new Product("productB", 20000, "https://plus.unsplash.com/premium_photo2");
        Product productC = new Product("productC", 30000, "https://plus.unsplash.com/premium_photo3");

        productRepository.save(productA);
        //하나만 있음 검증
        test(productA);

        //두개 있음 검증
        productRepository.save(productB);
        test(productA, productB);

        //세개 있음 검증
        productRepository.save(productC);
        test(productA, productB, productC);
    }

    void test(Product... products) {
        List<Product> result = productRepository.findAll();
        assertThat(result).containsExactly(products);
    }

    @Test
    void delete() {
        //given
        Product product = new Product("productA", 10000, "https://plus.unsplash.com/premium_photo1");
        productRepository.save(product);

        //when
        productRepository.deleteById(product.getId());

        //then
        assertThat(productRepository.findById(product.getId())).isEmpty();
        assertThat(productRepository.findAll()).isEmpty();
    }
}
