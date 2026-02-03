package Nhom6.TruongVuMinhVan_3646.controllers;

import Nhom6.TruongVuMinhVan_3646.entities.Book;
import Nhom6.TruongVuMinhVan_3646.daos.Item;
import Nhom6.TruongVuMinhVan_3646.services.BookService;
import Nhom6.TruongVuMinhVan_3646.services.CartService;
import Nhom6.TruongVuMinhVan_3646.services.CategoryService;
import jakarta.servlet.http.HttpSession;
import Nhom6.TruongVuMinhVan_3646.repositories.ICategoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
        private final BookService bookService;
        private final ICategoryRepository categoryRepository;
        private final CartService cartService;
        private final CategoryService categoryService;
        private static final Logger logger = LoggerFactory.getLogger(BookController.class);

        @GetMapping
        public String showAllBooks(Model model,
                        @RequestParam(defaultValue = "0") Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize,
                        @RequestParam(defaultValue = "id") String sortBy) {
                logger.info("Entering showAllBooks - pageNo: {}, pageSize: {}, sortBy: {}", pageNo, pageSize, sortBy);
                try {
                        Page<Book> bookPage = bookService.getAllBooks(pageNo, pageSize, sortBy);
                        model.addAttribute("books", bookPage.getContent());
                        model.addAttribute("currentPage", pageNo);
                        model.addAttribute("totalPages", bookPage.getTotalPages());
                        model.addAttribute("categories", categoryService.getAllCategories());
                        return "book/list";
                } catch (Exception e) {
                        logger.error("Error in showAllBooks: ", e);
                        model.addAttribute("error", e.getMessage());
                        return "error/500";
                }
        }

        @GetMapping("/add")
        public String addBookForm(Model model) {
                model.addAttribute("book", new Book());
                model.addAttribute("categories",
                                categoryService.getAllCategories());
                return "book/add";
        }

        @PostMapping("/add")
        public String addBook(
                        @Valid @ModelAttribute("book") Book book,
                        BindingResult bindingResult,
                        Model model) {
                if (bindingResult.hasErrors()) {
                        var errors = bindingResult.getAllErrors()
                                        .stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .toArray(String[]::new);
                        model.addAttribute("errors", errors);
                        model.addAttribute("categories",
                                        categoryService.getAllCategories());
                        return "book/add";
                }
                bookService.addBook(book);
                return "redirect:/books";
        }

        @PostMapping("/add-to-cart")
        public String addToCart(HttpSession session,
                        @RequestParam long id,
                        @RequestParam String name,
                        @RequestParam double price,
                        @RequestParam(defaultValue = "1") int quantity) {
                var cart = cartService.getCart(session);
                cart.addItems(new Item(id, name, price, quantity));
                cartService.updateCart(session, cart);
                return "redirect:/books";
        }

        @GetMapping("/edit/{id}")
        public String editBookForm(Model model, @PathVariable long id) {
                var book = bookService.getBookById(id);
                model.addAttribute("book", book.orElseThrow(() -> new IllegalArgumentException("Book not found")));
                model.addAttribute("categories", categoryService.getAllCategories());
                return "book/edit";
        }

        @PostMapping("/edit")
        public String editBook(@Valid @ModelAttribute("book") Book book,
                        BindingResult bindingResult,
                        Model model) {
                if (bindingResult.hasErrors()) {
                        var errors = bindingResult.getAllErrors()
                                        .stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .toArray(String[]::new);
                        model.addAttribute("errors", errors);
                        model.addAttribute("categories",
                                        categoryService.getAllCategories());
                        return "book/edit";
                }
                bookService.updateBook(book);
                return "redirect:/books";
        }

        @GetMapping("/delete/{id}")
        public String deleteBook(@PathVariable long id) {
                bookService.getBookById(id)
                                .ifPresentOrElse(
                                                book -> bookService.deleteBookById(id),
                                                () -> {
                                                        throw new IllegalArgumentException("Book not found");
                                                });
                return "redirect:/books";
        }

        @GetMapping("/search")
        public String searchBook(
                        @NotNull Model model,
                        @RequestParam String keyword,
                        @RequestParam(defaultValue = "0") Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize,
                        @RequestParam(defaultValue = "id") String sortBy) {
                // Simple search for now, could be paginated search if repository supports it
                var searchResults = bookService.searchBook(keyword);
                model.addAttribute("books", searchResults);
                model.addAttribute("currentPage", pageNo);
                model.addAttribute("totalPages", 1); // For search, just show one page or implement paginated search
                model.addAttribute("categories", categoryService.getAllCategories());
                return "book/list";
        }

}