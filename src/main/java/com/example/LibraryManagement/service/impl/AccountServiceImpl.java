package com.example.LibraryManagement.service.impl;

import com.example.LibraryManagement.dto.LoginRequestDTO;
import com.example.LibraryManagement.dto.RegisterRequestDTO;
import com.example.LibraryManagement.repository.AccountRepository;
import com.example.LibraryManagement.dto.AccountDTO;
import com.example.LibraryManagement.mapper.AccountMapper;
import com.example.LibraryManagement.entity.Account;
import com.example.LibraryManagement.service.AccountService;

import org.springframework.stereotype.Service;
import lombok.*;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountDTO register(RegisterRequestDTO request) {
        Account account = Account.builder()
                .username(request.getUsername())
                .password(request.getPassword())  // nên mã hóa sau này
                .displayName(request.getDisplayName())
                .title(request.getTitle())
                .build();
        return AccountMapper.toDTO(accountRepository.save(account));
    }

    @Override
    public AccountDTO login(LoginRequestDTO request) {
        Account account = accountRepository
                .findByUsernameAndPassword(request.getUsername(), request.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        return AccountMapper.toDTO(account);
    }

    @Override
    public AccountDTO getByUsername(String username) {
        Account account = accountRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return AccountMapper.toDTO(account);
    }

    @Override
    public Account getEntityByUsername(String username){
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
}

