package com.example.LibraryManagement.controller.api;

import com.example.LibraryManagement.dto.RecordsDTO;
import com.example.LibraryManagement.service.RecordsService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordsApiController {

    private final RecordsService recordsService;

    // Get all records with pagination
    @GetMapping
    public ResponseEntity<Page<RecordsDTO>> getAllRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer status) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RecordsDTO> records = recordsService.getAllRecords(pageable, search, status);
        return ResponseEntity.ok(records);
    }

    // Borrow a book
    @PostMapping("/borrow")
    public ResponseEntity<String> borrowBook(@RequestBody Map<String, Long> payload) {
        Long borrowerId = payload.get("borrowerId");
        Long bookId = payload.get("bookId");

        if (borrowerId == null || bookId == null) {
            return ResponseEntity.badRequest().body("Borrower ID and Book ID are required");
        }

        try {
            recordsService.borrowBook(borrowerId, bookId);
            return ResponseEntity.ok("Book borrowed successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Return a book
    @PostMapping("/return/{id}")
    public ResponseEntity<String> returnBook(@PathVariable Long id) {
        try {
            recordsService.returnBook(id);
            return ResponseEntity.ok("Book returned successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get overdue records with pagination
    @GetMapping("/overdue")
    public ResponseEntity<Page<RecordsDTO>> getOverdueRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RecordsDTO> overdueRecords = recordsService.getOverdueRecordsPaged(pageable);
        return ResponseEntity.ok(overdueRecords);
    }

    // Get loans today count
    @GetMapping("/today")
    public ResponseEntity<Long> getLoansToday() {
        long count = recordsService.getLoansToday();
        return ResponseEntity.ok(count);
    }
}