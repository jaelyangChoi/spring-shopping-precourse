package shopping.repository;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductUpdateDto {
    private String productName;
    private Integer price;
    private String imageUrl;

    public ProductUpdateDto(String productName, Integer price, String imageUrl) {
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
