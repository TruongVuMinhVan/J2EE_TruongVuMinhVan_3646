package Nhom6.TruongVuMinhVan_3646.validators;

import Nhom6.TruongVuMinhVan_3646.repositories.IUserRepository;
import Nhom6.TruongVuMinhVan_3646.validators.annotations.ValidPhone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidPhoneValidator implements ConstraintValidator<ValidPhone, String> {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (userRepository == null)
            return true;
        if (phone == null)
            return true;
        return userRepository.findByPhone(phone).isEmpty();
    }
}
