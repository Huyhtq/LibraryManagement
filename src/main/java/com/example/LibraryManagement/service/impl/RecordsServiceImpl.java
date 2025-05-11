package com.example.LibraryManagement.service.impl;

import com.example.LibraryManagement.repository.BorrowerRepository;
import com.example.LibraryManagement.repository.BookRepository;
import com.example.LibraryManagement.repository.RecordsRepository;
import com.example.LibraryManagement.dto.RecordsDTO;
import com.example.LibraryManagement.mapper.RecordsMapper;
import com.example.LibraryManagement.entity.Records;
import com.example.LibraryManagement.entity.Book;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.*;
import lombok.*;

@Service
@RequiredArgsConstructor
public class RecordsServiceImpl {

    private final RecordsRepository recordsRepository;
    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;

    public List<RecordsDTO> getAllRecords() {
        return recordsRepository.findAll().stream()
                .map(RecordsMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RecordsDTO getRecordById(Long id) {
        return recordsRepository.findById(id)
                .map(RecordsMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Record not found"));
    }

    public RecordsDTO createRecord(RecordsDTO dto) {
        // Reduce quantity
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if (book.getQuantity() <= 0) throw new RuntimeException("Book out of stock");
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        Records record = RecordsMapper.toEntity(dto);
        record.setBook(book);
        record.setBorrower(borrowerRepository.findById(dto.getBorrowerId())
                .orElseThrow(() -> new RuntimeException("Borrower not found")));

        return RecordsMapper.toDTO(recordsRepository.save(record));
    }

    public RecordsDTO updateRecord(Long id, RecordsDTO dto) {
        Records record = recordsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));
        record.setStatus(dto.getStatus());
        record.setReturn_date(dto.getReturnDate());
        return RecordsMapper.toDTO(recordsRepository.save(record));
    }

    public void deleteRecord(Long id) {
        recordsRepository.deleteById(id);
    }
}

