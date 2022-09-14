package umc.healthper.global.collectionValid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.Validation;
import java.util.Collection;

@Component
@Slf4j
@RequiredArgsConstructor
public class ElementValidator implements Validator {
    private final SpringValidatorAdapter validator;

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(target instanceof Collection){
            Collection collection = (Collection) target;

            for (Object o : collection) {
                validator.validate(o,errors);
            }
        }
        else validator.validate(target,errors);
    }
}
