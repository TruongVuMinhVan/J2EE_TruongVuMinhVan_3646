package Nhom6.TruongVuMinhVan_3646.services;

import Nhom6.TruongVuMinhVan_3646.entities.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final List<Book> books;

    public List<Book> getAllBooks() {
        return books;
    }

    public java.util.Optional<Book> getBookById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

    public void addBook(Book book) {
        if (book.getId() == null) {
            Long maxId = books.stream()
                    .map(Book::getId)
                    .max(Long::compare)
                    .orElse(0L);
            book.setId(maxId + 1);
        }
        books.add(book);
    }

    public void updateBook(Book book) {
        books.stream()
                .filter(b -> b.getId().equals(book.getId()))
                .findFirst()
                .ifPresent(b -> {
                    b.setTitle(book.getTitle());
                    b.setAuthor(book.getAuthor());
                    b.setPrice(book.getPrice());
                    b.setCategory(book.getCategory());
                });
    }

    public void deleteBookById(Long id) {
        books.removeIf(book -> book.getId().equals(id));
    }
}