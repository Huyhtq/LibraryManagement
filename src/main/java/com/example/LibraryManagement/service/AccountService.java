package com.example.LibraryManagement.service;

import com.example.LibraryManagement.dto.AccountDTO;
import com.example.LibraryManagement.dto.LoginRequestDTO;
import com.example.LibraryManagement.dto.RegisterRequestDTO;

public interface AccountService {
    AccountDTO register(RegisterRequestDTO request);
    AccountDTO login(LoginRequestDTO request);
    AccountDTO getByUsername(String username);
}
