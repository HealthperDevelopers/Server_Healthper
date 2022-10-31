package umc.healthper;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class ValidatorClassTypeTest {
    @BeforeClass
    public static void beforeClass() {
        Locale.setDefault(Locale.US); // locale 설정에 따라 에러 메시지가 달라진다.
    }

    @Test
    public void test_validate() {
        // Given
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        log.info("find why:{}", validator.getClass());
        final CreateContact createContact = CreateContact
                .builder()
                .uid(null) // @NotBlank가 정의되어 있기때문에 null이 오면 안된다.
                .contact("000")
                .build();

        // When
        final Collection<ConstraintViolation<CreateContact>> constraintViolations = validator.validate(createContact);

        // Then
        assertEquals(1, constraintViolations.size());  // ConstraintViolation에 실패에 대한 정보가 담긴다.
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }
    @Builder
    public static class CreateContact {
        @Length(max = 64) // 최대 길이 64
        @NotBlank // 빈문자열은 안됨
        private String uid;
        @Length(max = 1_600) // 최대 길이 1,600
        private String contact;
    }
}
