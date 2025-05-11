package com.example.LibraryManagement.entity;

import java.util.Date;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Records {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private Borrower borrower;

    @Temporal(TemporalType.DATE)
    private Date borrow_date;

    @Temporal(TemporalType.DATE)
    private Date return_date;

    private Integer status;
}
