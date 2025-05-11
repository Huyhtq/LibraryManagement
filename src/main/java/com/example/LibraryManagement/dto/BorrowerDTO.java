package com.example.LibraryManagement.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowerDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Integer borrowCount;
}