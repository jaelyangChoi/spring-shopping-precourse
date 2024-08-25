package shopping.web.exHandler.advice;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shopping.web.ProductController;
import shopping.web.exHandler.ErrorResult;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = ProductController.class)
public class ProductControllerAdvice {

    private final MessageSource ms;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResult NoSuchExHandle(NoSuchElementException e){
        log.error("[exceptionHandler] NoSuchExHandle", e);
        return new ErrorResult("NoSuchElement", ms.getMessage("noSuch.Product", null, null));
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorResult validationExHandle(ValidationException e){
        log.error("[exceptionHandler] ValidationExHandle", e);
        return new ErrorResult("Validation", ms.getMessage(e.getMessage(), null, null));
    }
}
