package shopping.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductUpdateDto {
    private String name;
    private Integer price;
    private String imageUrl;
}
