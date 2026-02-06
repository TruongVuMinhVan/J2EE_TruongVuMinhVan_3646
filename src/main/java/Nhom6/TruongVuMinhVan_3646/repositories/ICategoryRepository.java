package Nhom6.TruongVuMinhVan_3646.repositories;

import Nhom6.TruongVuMinhVan_3646.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ICategoryRepository extends
                JpaRepository<Category, Long> {
        // Get active categories only (for user-facing pages)
        List<Category> findByIsDeletedFalse();
}