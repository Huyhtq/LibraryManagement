package com.example.LibraryManagement.mapper;

import com.example.LibraryManagement.dto.BorrowerDTO;
import com.example.LibraryManagement.entity.Borrower;

public class BorrowerMapper {

    // Entity → DTO
    public static BorrowerDTO toDTO(Borrower borrower) {
        return BorrowerDTO.builder()
                .id(borrower.getId())
                .name(borrower.getName())
                .email(borrower.getEmail())
                .phone(borrower.getPhone())
                .borrowCount(borrower.getBorrowCount())
                .build();
    }

    // DTO → Entity
    public static Borrower toEntity(BorrowerDTO dto) {
        return Borrower.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .borrowCount(dto.getBorrowCount())
                .build();
    }
}
