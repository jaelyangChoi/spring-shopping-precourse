package shopping.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;
import shopping.domain.Product;
import shopping.dto.ProductDto;
import shopping.service.ProductService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final View error;
    private ProductService productService;

    public ProductController(ProductService productService, View error) {
        this.productService = productService;
        this.error = error;
    }

    @PostMapping
    public String createProduct(@RequestBody @Valid ProductDto saveParam, BindingResult bindingResult) {
        productService.save(saveParam);
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error ->
                    {
                        throw new IllegalArgumentException(error.getDefaultMessage());
                    }
            );
        }
        return "success";
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
    public String updateProduct(@PathVariable("productId") Long productId, @RequestBody ProductDto updateParam) {
        try {
            productService.updateProduct(productId, updateParam);
        } catch (NoSuchElementException e) {
            log.error("updateProduct : NoSuchElementException", e);
            return "Product not found";
        }

        return "success";
    }

    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return "success";
    }

}
