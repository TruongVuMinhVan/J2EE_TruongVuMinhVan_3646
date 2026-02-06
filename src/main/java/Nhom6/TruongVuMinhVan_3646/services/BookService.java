package Nhom6.TruongVuMinhVan_3646.services;

import Nhom6.TruongVuMinhVan_3646.entities.Book;
import Nhom6.TruongVuMinhVan_3646.repositories.IBookRepository;
import lombok.RequiredArgsConstructor;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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

    public Page<Book> getAllBooks(Integer pageNo, Integer pageSize, String sortBy) {
        logger.debug("Fetching books with pagination - pageNo: {}, pageSize: {}, sortBy: {}", pageNo, pageSize, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return bookRepository.findAll(pageRequest);
    }

    public void addBook(@NotNull Book book) {
        logger.debug("Adding new book: {}", book.getTitle());
        bookRepository.save(book);
        logger.debug("Book added successfully");
    }

    public Optional<Book> getBookById(Long id) {
        logger.debug("Fetching book with id: {}", id);
        return bookRepository.findById(id);
    }

    public void updateBook(@NotNull Book book) {
        Book existingBook = bookRepository.findById(book.getId())
                .orElse(null);
        Objects.requireNonNull(existingBook).setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());
        existingBook.setCategory(book.getCategory());
        if (book.getImage() != null) {
            existingBook.setImage(book.getImage());
        }
        bookRepository.save(existingBook);
    }

    public String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        Path uploadPath = Paths.get("src/main/resources/static/images/books");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filename;
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

    public List<Book> searchBook(String keyword) {
        return bookRepository.searchBook(keyword);
    }
}