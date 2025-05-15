package com.example.LibraryManagement.controller.api;

import com.example.LibraryManagement.dto.RecordsDTO;
import com.example.LibraryManagement.service.RecordsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/records")
public class RecordsApiController {

    private final RecordsService recordsService;

    @Autowired
    public RecordsApiController(RecordsService recordsService) {
        this.recordsService = recordsService;
    }

    // Get all records with pagination
    @GetMapping
    public ResponseEntity<Page<RecordsDTO>> getAllRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RecordsDTO> records = recordsService.getAllRecords(pageable);
        return ResponseEntity.ok(records);
    }

    // Get record by ID
    @GetMapping("/{id}")
    public ResponseEntity<RecordsDTO> getRecordById(@PathVariable Long id) {
        try {
            RecordsDTO dto = recordsService.getRecordById(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Borrow a book
    @PostMapping("/borrow")
    public ResponseEntity<RecordsDTO> borrowBook(@RequestParam Long bookId, @RequestParam Long borrowerId) {
        try {
            RecordsDTO dto = recordsService.borrowBook(bookId, borrowerId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Return a book
    @PostMapping("/return/{id}")
    public ResponseEntity<?> returnBook(@PathVariable Long id) {
        try {
            recordsService.returnBook(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Create a new record
    @PostMapping
    public ResponseEntity<RecordsDTO> createRecord(@RequestBody RecordsDTO dto) {
        if (dto.getBookId() == null || dto.getBorrowerId() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        RecordsDTO createdDto = recordsService.createRecord(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    // Update an existing record
    @PutMapping("/{id}")
    public ResponseEntity<RecordsDTO> updateRecord(@PathVariable Long id, @RequestBody RecordsDTO dto) {
        if (!id.equals(dto.getId())) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            RecordsDTO updatedDto = recordsService.updateRecord(id, dto);
            return ResponseEntity.ok(updatedDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    // Delete a record
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable Long id) {
        try {
            recordsService.deleteRecord(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Get overdue books count
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
        long count = recordsService.getLoansToday(); // Assume added in service
        return ResponseEntity.ok(count);
    }
}