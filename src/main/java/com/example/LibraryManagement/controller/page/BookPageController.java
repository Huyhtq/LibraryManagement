package com.example.LibraryManagement.controller.page;

import com.example.LibraryManagement.dto.BookDTO;
import com.example.LibraryManagement.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookPageController {

    private final BookService bookService;

    // <-- Hiển thị danh sách sách -->
    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("bookList", bookService.getAllBooks());
        return "books"; // Tên file Thymeleaf: books.html
    }

    // <-- Hiển thị form thêm mới -->
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        BookDTO book = new BookDTO();
        model.addAttribute("book", book);
        model.addAttribute("formTitle", "Add New Book");
        return "book-form";
    }

    // <-- Hiển thị form chỉnh sửa -->
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        BookDTO dto = bookService.getBookById(id);
        model.addAttribute("book", dto);
        model.addAttribute("formTitle", "Edit Book");
        return "book-form"; // Dùng chung file form
    }

    // <-- Xử lý lưu (tạo mới hoặc cập nhật) -->
    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") BookDTO bookDTO) {
        if (bookDTO.getId() == null) {
            bookService.createBook(bookDTO);
        } else {
            bookService.updateBook(bookDTO.getId(), bookDTO);
        }
        return "redirect:/books"; // Sau khi lưu xong thì quay lại danh sách
    }

    // <-- Xử lý xóa -->
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}
