package shopping.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import shopping.domain.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @NotBlank
    @Length(max = 15, message = "상품의 이름은 15자를 넘길 수 없습니다.")
    @SpecialChar
    private String name;
    @NotNull
    private Integer price;
    @NotBlank
    private String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public static ProductDto fromEntity(Product product) {
        return new ProductDto(product.getName(), product.getPrice(), product.getImageUrl());
    }

    public Product toEntity() {
        return new Product(name, price, imageUrl);
    }
}

