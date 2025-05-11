package com.example.LibraryManagement.service.impl;

import com.example.LibraryManagement.repository.BorrowerRepository;
import com.example.LibraryManagement.dto.BorrowerDTO;
import com.example.LibraryManagement.mapper.BorrowerMapper;
import com.example.LibraryManagement.entity.Borrower;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.*;
import lombok.*;

@Service
@RequiredArgsConstructor
public class BorrowerServiceImpl {

    private final BorrowerRepository borrowerRepository;

    public List<BorrowerDTO> getAllBorrowers() {
        return borrowerRepository.findAll().stream()
                .map(BorrowerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BorrowerDTO getBorrowerById(Long id) {
        return borrowerRepository.findById(id)
                .map(BorrowerMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));
    }

    public BorrowerDTO createBorrower(BorrowerDTO dto) {
        Borrower borrower = BorrowerMapper.toEntity(dto);
        return BorrowerMapper.toDTO(borrowerRepository.save(borrower));
    }

    public BorrowerDTO updateBorrower(Long id, BorrowerDTO dto) {
        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));
        borrower.setName(dto.getName());
        borrower.setEmail(dto.getEmail());
        borrower.setPhone(dto.getPhone());
        borrower.setBorrowCount(dto.getBorrowCount());
        return BorrowerMapper.toDTO(borrowerRepository.save(borrower));
    }

    public void deleteBorrower(Long id) {
        borrowerRepository.deleteById(id);
    }
}

