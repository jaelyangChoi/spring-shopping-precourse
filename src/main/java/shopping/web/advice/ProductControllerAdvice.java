package shopping.web.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shopping.web.ProductController;

@Slf4j
@RestControllerAdvice(assignableTypes = ProductController.class)
public class ProductControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String badRequestExceptionHandler(IllegalArgumentException e) {
        log.error("[badRequestExceptionHandler] ex", e);
        return e.getMessage();
    }


}
