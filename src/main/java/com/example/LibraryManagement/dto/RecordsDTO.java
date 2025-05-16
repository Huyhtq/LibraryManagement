package com.example.LibraryManagement.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordsDTO {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long borrowerId;
    private String borrowerName;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private Integer status;
}

