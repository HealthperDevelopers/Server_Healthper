package umc.healthper.global.collectionValid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class CollectionValidator implements ConstraintValidator<CustomValid, Object> {

    private final ElementValidator validator;

    @Override
    public void initialize(CustomValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object values, ConstraintValidatorContext context) {
        DataBinder dataBinder = new DataBinder(values);
        BindingResult bindingResult = dataBinder.getBindingResult();
        validator.validate(values,bindingResult);
        if(bindingResult.hasErrors())return false;
        return true;
    }

}
