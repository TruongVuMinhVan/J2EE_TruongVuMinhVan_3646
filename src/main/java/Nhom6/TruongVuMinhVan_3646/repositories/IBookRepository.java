package Nhom6.TruongVuMinhVan_3646.repositories;

import Nhom6.TruongVuMinhVan_3646.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b.title LIKE %?1% OR b.author LIKE %?1% OR b.category.name LIKE %?1%")
    List<Book> searchBook(String keyword);
}