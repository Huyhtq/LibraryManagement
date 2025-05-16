package com.example.LibraryManagement.service;

import com.example.LibraryManagement.dto.RecordsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecordsService {
    Page<RecordsDTO> getAllRecords(Pageable pageable, String search, Integer status);
    void borrowBook(Long bookId, Long borrowerId);
    void returnBook(Long id);
    Page<RecordsDTO> getOverdueRecordsPaged(Pageable pageable);
    long getLoansToday();
}