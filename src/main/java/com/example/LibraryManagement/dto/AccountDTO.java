package com.example.LibraryManagement.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {
    private Long id;
    private String username;
    private String displayName;
    private String title;
}
