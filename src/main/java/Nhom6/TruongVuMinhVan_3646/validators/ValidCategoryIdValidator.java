package Nhom6.TruongVuMinhVan_3646.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import Nhom6.TruongVuMinhVan_3646.validators.annotations.ValidCategoryId;
import Nhom6.TruongVuMinhVan_3646.entities.Category;

public class ValidCategoryIdValidator implements ConstraintValidator<ValidCategoryId, Category> {
    @Override
    public void initialize(ValidCategoryId annotation) {
        ConstraintValidator.super.initialize(annotation);
    }

    @Override
    public boolean isValid(Category value, ConstraintValidatorContext context) {
        // Null values are valid by default (use @NotNull to ensure non-null)
        if (value == null) {
            return true;
        }
        // Category must have a valid ID
        return value.getId() != null;
    }
}
