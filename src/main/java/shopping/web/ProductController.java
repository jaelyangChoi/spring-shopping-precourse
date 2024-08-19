package shopping.web;

import org.springframework.web.bind.annotation.*;
import shopping.domain.Product;
import shopping.dto.ProductDto;
import shopping.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ProductDto findProduct(@PathVariable("productId") Long productId) {
        return ProductDto.fromEntity(productService.findProduct(productId));
    }

    @GetMapping
    public List<ProductDto> findAllProduct() {
        List<Product> products = productService.findAllProducts();
        return products.stream().map(ProductDto::fromEntity).collect(Collectors.toList());
    }

    @PutMapping("/{productId}")
    public String updateProduct(@PathVariable Long productId, @RequestBody ProductDto updateParam) {

        return null;
    }

    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable Long productId) {

        return null;
    }

}
