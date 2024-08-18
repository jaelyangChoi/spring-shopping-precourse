package shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import shopping.repository.MemberRepository;
import shopping.repository.ProductRepository;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Profile("local")
    public TestDataInit testDataInit(ProductRepository productRepository, MemberRepository memberRepository) {
        return new TestDataInit(productRepository);
    }
}
