package com.example.LibraryManagement.mapper;

import com.example.LibraryManagement.dto.RecordsDTO;
import com.example.LibraryManagement.entity.Records;
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
                .build();
    }

    // DTO → Entity
    public static Records toEntity(RecordsDTO dto) {
        Book book = new Book();
        book.setId(dto.getBookId());

        Borrower borrower = new Borrower();
        borrower.setId(dto.getBorrowerId());

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
