package com.example.LibraryManagement.mapper;

import com.example.LibraryManagement.dto.RecordsDTO;
import com.example.LibraryManagement.entity.Records;
import com.example.LibraryManagement.repository.BookRepository;
import com.example.LibraryManagement.repository.BorrowerRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class RecordsMapper {
    public static RecordsDTO toDTO(Records record) {
        LocalDate borrowDate = record.getBorrowDate() != null ?
                new java.util.Date(record.getBorrowDate().getTime())
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate() : null;
        LocalDate returnDate = record.getReturnDate() != null ?
                new java.util.Date(record.getReturnDate().getTime())
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate() : null;
                    
        return RecordsDTO.builder()
                .id(record.getId())
                .bookId(record.getBook().getId())
                .bookTitle(record.getBook().getTitle())
                .borrowerId(record.getBorrower().getId())
                .borrowerName(record.getBorrower().getName())
                .borrowDate(borrowDate)
                .returnDate(returnDate)
                .status(record.getStatus())
                .build();
    }

    public static Records toEntity(RecordsDTO dto, BookRepository bookRepository, BorrowerRepository borrowerRepository) {
        var book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Book with ID " + dto.getBookId() + " not found"));
        var borrower = borrowerRepository.findById(dto.getBorrowerId())
                .orElseThrow(() -> new IllegalArgumentException("Borrower with ID " + dto.getBorrowerId() + " not found"));

        Date borrowDate = dto.getBorrowDate() != null ?
                Date.from(dto.getBorrowDate().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
        Date returnDate = dto.getReturnDate() != null ?
                Date.from(dto.getReturnDate().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
        
        return Records.builder()
                .id(dto.getId())
                .book(book)
                .borrower(borrower)
                .borrowDate(borrowDate)
                .returnDate(returnDate)
                .status(dto.getStatus())
                .build();
    }
}