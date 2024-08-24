package shopping.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import shopping.domain.Product;

@Data
@NoArgsConstructor
public class ProductUpdateDto {

    @NotBlank
    @Length(max=15)
    private String productName;
    @NotNull
    private Integer price;
    @NotBlank
    private String imageUrl;

    public ProductUpdateDto(String productName, Integer price, String imageUrl) {
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product toEntity() {
        return new Product(this.productName, this.price, this.imageUrl);
    }
}
