package Nhom6.TruongVuMinhVan_3646.services;

import Nhom6.TruongVuMinhVan_3646.entities.Category;
import Nhom6.TruongVuMinhVan_3646.repositories.ICategoryRepository;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { Exception.class, Throwable.class })
public class CategoryService {
    private final ICategoryRepository categoryRepository;

    // For Admin: Get all categories including deleted ones
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // For User: Get only active (non-deleted) categories
    public List<Category> getActiveCategories() {
        return categoryRepository.findByIsDeletedFalse();
    }

    public Optional<Category> getCategoryById(@NotNull Long id) {
        return categoryRepository.findById(id);
    }

    public void addCategory(@NotNull Category category) {
        categoryRepository.save(category);
    }

    public void updateCategory(@NotNull Category category) {
        Category existingCategory = categoryRepository
                .findById(category.getId())
                .orElse(null);
        Objects.requireNonNull(existingCategory)
                .setName(category.getName());
        categoryRepository.save(existingCategory);
    }

    // Soft delete: Mark category as deleted instead of removing from DB
    public void deleteCategoryById(@NotNull Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        category.setDeleted(true);
        categoryRepository.save(category);
    }

    // Restore deleted category
    public void restoreCategoryById(@NotNull Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        category.setDeleted(false);
        categoryRepository.save(category);
    }
}
