package umc.healthper.global.collectionValid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CollectionValidator.class)
@Target({ElementType.TYPE_USE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomValid {
    String message() default "json 입력 오류";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
