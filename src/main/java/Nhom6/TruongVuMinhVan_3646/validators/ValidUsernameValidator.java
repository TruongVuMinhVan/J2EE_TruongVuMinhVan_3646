package Nhom6.TruongVuMinhVan_3646.validators;

import Nhom6.TruongVuMinhVan_3646.validators.annotations.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@org.springframework.stereotype.Component
public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {
    @org.springframework.beans.factory.annotation.Autowired
    private Nhom6.TruongVuMinhVan_3646.repositories.IUserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (userRepository == null) {
            return true;
        }
        if (username == null) {
            return true;
        }
        return userRepository.findByUsername(username).isEmpty();
    }
}