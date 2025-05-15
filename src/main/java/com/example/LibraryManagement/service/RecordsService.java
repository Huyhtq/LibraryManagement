package com.example.LibraryManagement.service;

import com.example.LibraryManagement.dto.RecordsDTO;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecordsService {
    List<RecordsDTO> getAllRecords();
    RecordsDTO getRecordById(Long id);
    RecordsDTO createRecord(RecordsDTO dto);
    RecordsDTO borrowBook(Long bookId, Long borrowerId);
    RecordsDTO updateRecord(Long id, RecordsDTO dto);
    void returnBook(Long id);
    void deleteRecord(Long id);
    long getOverdueBooks();
    Page<RecordsDTO> getAllRecords(Pageable pageable);
    long getLoansToday();
    Page<RecordsDTO> getOverdueRecordsPaged(Pageable pageable);
}
