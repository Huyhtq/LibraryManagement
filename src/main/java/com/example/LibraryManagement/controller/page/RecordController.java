package com.example.LibraryManagement.controller.page;

import com.example.LibraryManagement.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/records")
public class RecordController {

    private final RecordsService recordService;
    private final BookService bookService;
    private final BorrowerService borrowerService;

    @Autowired
    public RecordController(RecordsService recordService, BookService bookService, BorrowerService borrowerService) {
        this.recordService = recordService;
        this.bookService = bookService;
        this.borrowerService = borrowerService;
    }

   @GetMapping
    public String listRecords(Model model) {
        model.addAttribute("records", recordService.getAllRecords());
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("borrowers", borrowerService.getAllBorrowers());
        return "records";
    }

    @PostMapping("/borrow")
    public String borrowBook(@RequestParam Long bookId, @RequestParam Long borrowerId, RedirectAttributes redirectAttributes) {
        try {
            recordService.borrowBook(bookId, borrowerId);
            redirectAttributes.addFlashAttribute("success", "Book borrowed successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/records";
    }

    @GetMapping("/return/{id}")
    public String returnBook(@PathVariable Long id) {
        recordService.returnBook(id);
        return "redirect:/records";
    }
}