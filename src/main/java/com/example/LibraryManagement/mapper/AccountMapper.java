package com.example.LibraryManagement.mapper;

import com.example.LibraryManagement.dto.AccountDTO;
import com.example.LibraryManagement.entity.Account;

public class AccountMapper {

    // Entity → DTO
    public static AccountDTO toDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .username(account.getUsername())
                .displayName(account.getDisplayName())
                .title(account.getTitle())
                .build();
    }

    // DTO → Entity
    public static Account toEntity(AccountDTO dto) {
        return Account.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .displayName(dto.getDisplayName())
                .title(dto.getTitle())
                .build();
    }
}
