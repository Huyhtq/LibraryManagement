package com.example.LibraryManagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Borrower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    private String email;
    private String phone;

    @Column(name = "borrow_count", columnDefinition = "integer default 0")
    private Integer borrowCount;

}
