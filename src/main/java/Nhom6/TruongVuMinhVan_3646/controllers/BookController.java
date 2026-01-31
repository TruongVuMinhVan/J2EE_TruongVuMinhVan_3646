package Nhom6.TruongVuMinhVan_3646.controllers;

import Nhom6.TruongVuMinhVan_3646.entities.Book;
import Nhom6.TruongVuMinhVan_3646.services.BookService;
import Nhom6.TruongVuMinhVan_3646.repositories.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final ICategoryRepository categoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @GetMapping
    public String showAllBooks(Model model) {
        logger.debug("Calling showAllBooks endpoint");
        try {
            var books = bookService.getAllBooks();
            logger.debug("Retrieved {} books from database", books.size());
            model.addAttribute("books", books);
            model.addAttribute("totalPages", 1);
            model.addAttribute("currentPage", 0);
            return "book/list";
        } catch (Exception e) {
            logger.error("Error in showAllBooks", e);
            throw e;
        }
    }

    @GetMapping("/edit/{id}")
    public String editBookForm(Model model, @PathVariable long id) {
        var book = bookService.getBookById(id).orElse(null);
        model.addAttribute("book", book != null ? book : new Book());
        model.addAttribute("categories", categoryRepository.findAll());
        return "book/edit";
    }

    @PostMapping("/edit")
    public String editBook(@ModelAttribute("book") Book book, @RequestParam(required = false) Long categoryId) {
        logger.debug("Editing book with id: {}, categoryId: {}", book.getId(), categoryId);
        if (categoryId != null && categoryId > 0) {
            var category = categoryRepository.findById(categoryId).orElse(null);
            book.setCategory(category);
            logger.debug("Category set to: {}", category);
        } else {
            book.setCategory(null);
            logger.debug("Category set to null");
        }
        bookService.updateBook(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        if (bookService.getBookById(id).isPresent())
            bookService.deleteBookById(id);
        return "redirect:/books";
    }
}