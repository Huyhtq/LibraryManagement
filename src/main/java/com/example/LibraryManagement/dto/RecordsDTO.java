package com.example.LibraryManagement.dto;

import lombok.*;
import java.util.Date;

import com.example.LibraryManagement.entity.Book;
import com.example.LibraryManagement.entity.Borrower;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordsDTO {
    private Long id;
    private Long bookId;
    private Long borrowerId;
    private Date borrowDate;
    private Date returnDate;
    private Integer status;
    private Book book;       
    private Borrower borrower;
}

