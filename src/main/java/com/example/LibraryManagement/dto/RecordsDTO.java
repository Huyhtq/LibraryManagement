package com.example.LibraryManagement.dto;

import lombok.*;
import java.util.Date;

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
}
