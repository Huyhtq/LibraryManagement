package com.example.LibraryManagement.controller;

import com.example.LibraryManagement.dto.AccountDTO;
import com.example.LibraryManagement.dto.LoginRequestDTO;
import com.example.LibraryManagement.dto.RegisterRequestDTO;
import com.example.LibraryManagement.service.AccountService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<AccountDTO> register(@RequestBody RegisterRequestDTO request) {
        AccountDTO result = accountService.register(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<AccountDTO> login(@RequestBody LoginRequestDTO request) {
        AccountDTO result = accountService.login(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{username}")
    public ResponseEntity<AccountDTO> getByUsername(@PathVariable String username) {
        AccountDTO result = accountService.getByUsername(username);
        return ResponseEntity.ok(result);
    }
}