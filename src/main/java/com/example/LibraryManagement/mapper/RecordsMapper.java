package com.example.LibraryManagement.mapper;

import com.example.LibraryManagement.dto.RecordsDTO;
import com.example.LibraryManagement.entity.Records;
import com.example.LibraryManagement.repository.BookRepository;
import com.example.LibraryManagement.repository.BorrowerRepository;
import com.example.LibraryManagement.entity.Book;
import com.example.LibraryManagement.entity.Borrower;

public class RecordsMapper {
    // Entity → DTO
    public static RecordsDTO toDTO(Records record) {
        return RecordsDTO.builder()
                .id(record.getId())
                .bookId(record.getBook().getId())
                .borrowerId(record.getBorrower().getId())
                .borrowDate(record.getBorrow_date())
                .returnDate(record.getReturn_date())
                .status(record.getStatus())
                .book(record.getBook())      // Add full book object
                .borrower(record.getBorrower()) // Add full borrower object
                .build();
    }

    // DTO → Entity
    public static Records toEntity(RecordsDTO dto, BookRepository bookRepository, BorrowerRepository borrowerRepository) {
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Book with ID " + dto.getBookId() + " not found"));
        Borrower borrower = borrowerRepository.findById(dto.getBorrowerId())
                .orElseThrow(() -> new IllegalArgumentException("Borrower with ID " + dto.getBorrowerId() + " not found"));

        return Records.builder()
                .id(dto.getId())
                .book(book)
                .borrower(borrower)
                .borrow_date(dto.getBorrowDate())
                .return_date(dto.getReturnDate())
                .status(dto.getStatus())
                .build();
    }   
}
