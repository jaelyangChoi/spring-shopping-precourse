package shopping.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import shopping.domain.Product;
import shopping.domain.dto.ProductUpdateDto;
import shopping.service.ProductService;
import shopping.web.validator.ProductValidator;

import java.util.List;
import java.util.NoSuchElementException;
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
    public Product findProduct(@PathVariable Long productId) {
        Optional<Product> findProduct = productService.findById(productId);
        return findProduct.orElse(null);
    }

    @GetMapping
    public List<Product> findAllProducts() {
        return productService.findAll();
    }

    @PostMapping
    public Object createProduct(@RequestBody @Validated ProductUpdateDto productDto, BindingResult bindingResult) {

        //특수문자, 비속어 검증
        productValidator.validate(productDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 erros={]", bindingResult);
            return bindingResult.getAllErrors();
        }

        return productService.save(productDto);
    }

    @PutMapping("/{productId}")
    public Object updateProduct(@PathVariable Long productId, @RequestBody @Validated ProductUpdateDto updateParam, BindingResult bindingResult) {

        //특수문자, 비속어 검증
        productValidator.validate(updateParam, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 erros={]", bindingResult);
            return bindingResult.getAllErrors();
        }

        try {
            productService.update(productId, updateParam);
            return "SUCCESS";
        } catch (NoSuchElementException e) {
            log.error("UPDATE ERROR : ", e);
            return "NO SUCH PRODUCT";
        }
    }

    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable Long productId) {
        productService.delete(productId);
        return "OK";
    }
}
