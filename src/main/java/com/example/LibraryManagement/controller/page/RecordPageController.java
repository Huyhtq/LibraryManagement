package com.example.LibraryManagement.controller.page;

import com.example.LibraryManagement.dto.BookDTO;
import com.example.LibraryManagement.dto.BorrowerDTO;
import com.example.LibraryManagement.service.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/records")
@RequiredArgsConstructor
public class RecordPageController {

    private final RecordsService recordService;
    private final BookService bookService;
    private final BorrowerService borrowerService;

    @GetMapping
    public String listRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer status,
            Model model) {
        // Lấy danh sách borrowers và books để hiển thị trong form mượn sách
        List<BorrowerDTO> borrowers = borrowerService.getAllBorrowers();
        List<BookDTO> books = bookService.getAllBooks();

        // Lấy số lượng mượn hôm nay
        long loansToday = recordService.getLoansToday();

        // Thêm các thuộc tính vào model
        model.addAttribute("borrowers", borrowers);
        model.addAttribute("books", books);
        model.addAttribute("loansToday", loansToday);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("status", status);

        return "records";
    }

    @PostMapping("/borrow")
    public String borrowBook(@RequestParam Long borrowerId, @RequestParam Long bookId) {
        recordService.borrowBook(borrowerId, bookId);
        return "redirect:/records";
    }

    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable Long id) {
        recordService.returnBook(id);
        return "redirect:/records";
    }
}