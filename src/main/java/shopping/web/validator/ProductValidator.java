package shopping.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import shopping.domain.dto.ProductUpdateDto;

@Component
public class ProductValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ProductUpdateDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductUpdateDto productDto = (ProductUpdateDto) target;
        String name = productDto.getName();
        //특수 문자
        if(hasInvalidSpecialChar(name)){
            errors.rejectValue("name", "specialChar", new Object[]{productDto.getName()}, null);
        }
        //비속어
        if(hasProfanity(name) ){
            errors.rejectValue("name", "specialChar", new Object[]{productDto.getName()}, null);
        }

    }

    private boolean hasProfanity(String name) {
        return false;
    }

    private boolean hasInvalidSpecialChar(String name) {
        return false;
    }
}
