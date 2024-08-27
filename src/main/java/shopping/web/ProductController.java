package shopping.web;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import shopping.domain.Product;
import shopping.domain.dto.ProductUpdateDto;
import shopping.service.ProductService;
import shopping.web.validator.ProductValidator;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
@RestController
public class ProductController {

    private final ProductService productService;
    private final ProductValidator productValidator;

    @InitBinder //컨트롤러 호출시마다 호출됨. binder는 매번 생성됨
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(productValidator);
    }

    @GetMapping("/{productId}")
    public Product findProduct(@PathVariable("productId") Long productId) {
        Optional<Product> findProduct = productService.findById(productId);
        return findProduct.orElse(null);
    }

    @GetMapping
    public List<Product> findAllProducts() {
        return productService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody @Validated ProductUpdateDto productDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("bindingResult.getFieldErrosr()={}", bindingResult.getFieldErrors());
            throw new ValidationException(bindingResult.getFieldError().getCode());
        }

        return productService.save(productDto);
    }

    @PutMapping("/{productId}")
    public String updateProduct(@PathVariable("productId") Long productId, @RequestBody @Validated ProductUpdateDto updateParam, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("bindingResult.getFieldErrosr()={}", bindingResult.getFieldErrors());
            throw new ValidationException(bindingResult.getFieldError().getCode());
        }

        productService.update(productId, updateParam);
        return "SUCCESS";
    }

    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId) {
        productService.delete(productId);
        return "OK";
    }
}
