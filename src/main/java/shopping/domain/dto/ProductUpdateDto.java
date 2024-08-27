package shopping.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import shopping.domain.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDto {

    @NotBlank
    @Length(max=15)
    private String name;
    @NotNull
    private Integer price;
    @NotBlank
    private String imageUrl;

    public Product toEntity() {
        return new Product(this.name, this.price, this.imageUrl);
    }


}
