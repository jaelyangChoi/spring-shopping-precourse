package shopping.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shopping.domain.dto.ProductUpdateDto;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;

    public Product(String name, Integer price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public void update(ProductUpdateDto productUpdateDto) {
        if (productUpdateDto.getName() != null) {
            this.name = productUpdateDto.getName();
        }

        if (productUpdateDto.getPrice() != null) {
            this.price = productUpdateDto.getPrice();
        }

        if (productUpdateDto.getImageUrl() != null) {
            this.imageUrl = productUpdateDto.getImageUrl();
        }
    }
}
