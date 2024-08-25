package shopping.web.exHandler.advice;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shopping.web.MemberController;
import shopping.web.exHandler.ErrorResult;


@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = MemberController.class)
public class MemberControllerAdvice {
    private final MessageSource ms;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResult registerExHandle(RuntimeException e){
        log.error("[exceptionHandler] registerExHandle", e);
        return new ErrorResult("registered Email", ms.getMessage(e.getMessage(), null, null));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorResult validationExHandle(ValidationException e){
        log.error("[exceptionHandler] ValidationExHandle", e);
        return new ErrorResult("Validation", ms.getMessage(e.getMessage(), null, null));
    }
}
