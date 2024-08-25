package shopping.web.validator;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import shopping.domain.dto.ProductUpdateDto;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest
class ProductValidatorTest {

    @Autowired
    private ProductValidator validator;


    @Test
    void 특수문자_포함여부() {

        var okDto = new ProductUpdateDto("(할인) 아메리카노", 1, "url");
        var noDto = new ProductUpdateDto("아메리카노 *할인", 1, "url");

        Errors okErrors = new BeanPropertyBindingResult(ProductUpdateDto.class, "okDto");
        Errors noErrors = new BeanPropertyBindingResult(ProductUpdateDto.class, "noDto");

        validator.validate(okDto, okErrors);
        validator.validate(noDto, noErrors);

        log.info(okErrors.toString());
        log.info(noErrors.toString());

        assertThat(okErrors.hasErrors()).isFalse();
        assertThat(Objects.requireNonNull(noErrors.getFieldError()).getCode()).isEqualTo("specialChar");
    }

    @Test
    void 비속어_포함여부() {
        var okDto = new ProductUpdateDto("(할인) 아메리카노", 1, "url");
        var noDto = new ProductUpdateDto("아메리카노 *할인fuck", 1, "url");

        BindingResult bindingResult1 = new BeanPropertyBindingResult(ProductUpdateDto.class, "okDto");
        BindingResult bindingResult2 = new BeanPropertyBindingResult(ProductUpdateDto.class, "noDto");

        validator.validate(okDto, bindingResult1);
        validator.validate(noDto, bindingResult2);

        log.info(bindingResult1.toString());
        log.info(bindingResult2.toString());

        assertThat(bindingResult1.hasErrors()).isFalse();
        assertTrue(bindingResult2.getFieldErrors("name")
                .stream()
                .anyMatch(err -> err.getCode().equals("profanity")));
    }

}