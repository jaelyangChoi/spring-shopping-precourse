package shopping.web.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.client.RestClient;
import shopping.domain.dto.ProductUpdateDto;

@Slf4j
@Component
@Order  // default : 가장 낮은 우선순위 설정. 기본 bean validation 먼저 진행
public class ProductValidator implements Validator {
    private RestClient.Builder builder;
    private RestClient client;
    private String url;
    private ObjectMapper mapper;

    public ProductValidator(RestClient.Builder builder, ObjectMapper mapper) {
        this.mapper = mapper;
        this.builder = builder;
        client = builder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        url = "https://www.purgomalum.com/service/json?text=";
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return ProductUpdateDto.class.isAssignableFrom(clazz);
    }


    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        ProductUpdateDto productDto = (ProductUpdateDto) target;
        String name = productDto.getName();
        //특수 문자
        if (hasInvalidSpecialChar(name)) {
            log.info("특수 문자 포함 = {}", name);
            errors.rejectValue("name", "specialChar", new Object[]{productDto.getName()}, null);
        }
        //비속어
        if (hasProfanity(name)) {
            log.info("비속어 포함 = {}", name);
            errors.rejectValue("name", "profanity", new Object[]{productDto.getName()}, null);
        }
    }


    /**
     * 상품은 일부 특수 문자는 허용하지 않는다. ( ( ), [ ], +, -, &, /, _ 외)
     */
    private boolean hasInvalidSpecialChar(String input) {
        // 제외할 특수 문자들을 정의
        String excludedCharacters = "[\\(\\)\\[\\]\\+\\-\\&\\/\\_]";

        // 허용할 문자 범위와 제외된 특수 문자를 제외한 모든 특수 문자를 찾는 정규 표현식
        String specialCharactersPattern = "[^\\w\\s" + excludedCharacters + "\\uAC00-\\uD7A3]";

        // 입력 문자열이 제외된 특수 문자를 제외한 다른 특수 문자를 포함하는지 확인
        return input.matches(".*" + specialCharactersPattern + ".*");
    }

    /**
     * PurgoMalum에서 욕설이 포함되어 있는지 확인한다.
     */
    private boolean hasProfanity(String input) {

        ResponseEntity<String> response = client
                .get()
                .uri(url + input)
                .retrieve()
                .toEntity(String.class);

        try {
            String result = mapper.readTree(response.getBody()).path("result").asText();
            log.info(result);
            //변환된 문자열이면 비속어
            return !input.equals(result);
        } catch (JsonProcessingException e) {
            log.error("응답 값 파싱 오류 발생", e);
        }
        return false;
    }
}
