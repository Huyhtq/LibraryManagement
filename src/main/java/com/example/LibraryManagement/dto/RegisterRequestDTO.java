package com.example.LibraryManagement.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {
    private String username;
    private String password;
    private String displayName;
    private String title;  // Ví dụ: "admin", "staff"
}