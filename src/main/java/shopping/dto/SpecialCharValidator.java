package shopping.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SpecialCharValidator implements ConstraintValidator<SpecialChar, String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return containsInvalidSpecialCharacters(value);
    }

    private boolean containsInvalidSpecialCharacters(String input) {
        // 허용된 특수 문자들 (각각 이스케이프 처리)
        String allowedSpecialCharacters = "\\(\\)\\[]\\+-&/_";

        // 정규 표현식으로 허용된 특수 문자 외의 특수 문자를 체크
        String regex = "[^\\w\\s" + allowedSpecialCharacters + "]";

        return input.matches(".*" + regex + ".*");
    }
}
