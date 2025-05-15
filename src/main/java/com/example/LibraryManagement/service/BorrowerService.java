package com.example.LibraryManagement.service;

import com.example.LibraryManagement.dto.BorrowerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public interface BorrowerService {
    List<BorrowerDTO> getAllBorrowers();
    BorrowerDTO getBorrowerById(Long id);
    BorrowerDTO createBorrower(BorrowerDTO dto);
    BorrowerDTO updateBorrower(Long id, BorrowerDTO dto);
    void deleteBorrower(Long id);
    Page<BorrowerDTO> getAllBorrowers(Pageable pageable);
    BorrowerDTO getBorrowerByEmail(String email);
    List<BorrowerDTO> getBorrowersByMinBorrowCount(int minBorrowCount);
}
