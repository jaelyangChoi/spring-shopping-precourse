package shopping.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class DisplayedNameTest {
    @Test
    void 비속어_체크() {
        Profanity profanity = new FakeProfanity();

        Assertions.assertThatIllegalArgumentException().isThrownBy(
                () -> {
                    new Product(new DisplayedName("욕설", profanity), 10_000, "image1");
                });
    }

    class FakeProfanity implements Profanity {
        private final List<String> profanities = List.of("욕설", "비속어");

        @Override
        public boolean contains(String text) {
            return profanities.contains(text);
        }
    }
}