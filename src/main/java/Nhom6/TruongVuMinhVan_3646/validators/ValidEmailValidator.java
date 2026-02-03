package Nhom6.TruongVuMinhVan_3646.validators;

import Nhom6.TruongVuMinhVan_3646.repositories.IUserRepository;
import Nhom6.TruongVuMinhVan_3646.validators.annotations.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (userRepository == null)
            return true;
        if (email == null)
            return true;
        return userRepository.findByEmail(email).isEmpty();
    }
}
