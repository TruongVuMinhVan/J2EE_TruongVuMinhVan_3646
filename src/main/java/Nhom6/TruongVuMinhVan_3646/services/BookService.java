package Nhom6.TruongVuMinhVan_3646.services;

import Nhom6.TruongVuMinhVan_3646.entities.Book;
import Nhom6.TruongVuMinhVan_3646.repositories.IBookRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final IBookRepository bookRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    public List<Book> getAllBooks() {
        logger.debug("Fetching all books from repository");
        List<Book> books = bookRepository.findAll();
        logger.debug("Found {} books", books.size());
        return books;
    }

    public Optional<Book> getBookById(Long id) {
        logger.debug("Fetching book with id: {}", id);
        return bookRepository.findById(id);
    }

    public void updateBook(Book book) {
        logger.debug("Updating book with id: {}", book.getId());
        if (book.getId() != null && bookRepository.existsById(book.getId())) {
            bookRepository.save(book);
            logger.debug("Book updated successfully");
        } else {
            logger.warn("Book with id {} not found for update", book.getId());
        }
    }

    public void deleteBookById(Long id) {
        logger.debug("Deleting book with id: {}", id);
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            logger.debug("Book deleted successfully");
        } else {
            logger.warn("Book with id {} not found for deletion", id);
        }
    }
}