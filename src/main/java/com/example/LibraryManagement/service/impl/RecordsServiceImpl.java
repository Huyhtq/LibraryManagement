package com.example.LibraryManagement.service.impl;

import com.example.LibraryManagement.repository.BorrowerRepository;
import com.example.LibraryManagement.repository.BookRepository;
import com.example.LibraryManagement.repository.RecordsRepository;
import com.example.LibraryManagement.dto.RecordsDTO;
import com.example.LibraryManagement.mapper.RecordsMapper;
import com.example.LibraryManagement.entity.Records;
import com.example.LibraryManagement.entity.Book;
import com.example.LibraryManagement.entity.Borrower;
import com.example.LibraryManagement.service.RecordsService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecordsServiceImpl implements RecordsService {

    @Autowired
    private RecordsRepository recordsRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Override
    public List<RecordsDTO> getAllRecords() {
        return recordsRepository.findAllWithDetails().stream()
                .map(RecordsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecordsDTO getRecordById(Long id) {
        return recordsRepository.findById(id)
                .map(RecordsMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Record with ID " + id + " not found"));
    }

    @Override
    @Transactional
    public RecordsDTO createRecord(RecordsDTO dto) {
        Records record = RecordsMapper.toEntity(dto, bookRepository, borrowerRepository);

        Book book = record.getBook();
        Borrower borrower = record.getBorrower();

        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        borrower.setBorrowCount(borrower.getBorrowCount() + 1);
        borrowerRepository.save(borrower);

        return RecordsMapper.toDTO(recordsRepository.save(record));
    }

    @Override
    @Transactional
    public RecordsDTO borrowBook(Long bookId, Long borrowerId) {
        RecordsDTO dto = RecordsDTO.builder()
                .bookId(bookId)
                .borrowerId(borrowerId)
                .status(0)
                .borrowDate(new java.sql.Date(System.currentTimeMillis())) // 15/05/2025
                .build();
        return createRecord(dto);
    }

    @Override
    @Transactional
    public RecordsDTO updateRecord(Long id, RecordsDTO dto) {
        Records record = recordsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record with ID " + id + " not found"));

        if (record.getStatus() == 1) {
            throw new IllegalStateException("Book has already been returned");
        }

        record.setStatus(dto.getStatus());
        if (dto.getStatus() == 1) {
            record.setReturn_date(new java.sql.Date(System.currentTimeMillis())); // 15/05/2025

            Book book = bookRepository.findById(record.getBook().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Book not found"));
            book.setQuantity(book.getQuantity() + 1);
            bookRepository.save(book);

            Borrower borrower = borrowerRepository.findById(record.getBorrower().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Borrower not found"));
            borrower.setBorrowCount(borrower.getBorrowCount() - 1);
            borrowerRepository.save(borrower);
        }

        return RecordsMapper.toDTO(recordsRepository.save(record));
    }

    @Override
    @Transactional
    public void returnBook(Long id) {
        RecordsDTO dto = getRecordById(id);
        dto.setStatus(1);
        updateRecord(id, dto);
    }

    @Override
    @Transactional
    public void deleteRecord(Long id) {
        Records record = recordsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record with ID " + id + " not found"));

        if (record.getStatus() == 0) {
            Book book = bookRepository.findById(record.getBook().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Book not found"));
            book.setQuantity(book.getQuantity() + 1);
            bookRepository.save(book);

            Borrower borrower = borrowerRepository.findById(record.getBorrower().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Borrower not found"));
            borrower.setBorrowCount(borrower.getBorrowCount() - 1);
            borrowerRepository.save(borrower);
        }

        recordsRepository.deleteById(id);
    }

    @Override
    public long getOverdueBooks() {
        LocalDate today = LocalDate.now(); // 15/05/2025
        LocalDate overdueDate = today.minusDays(14);
        Date overdueDateAsDate = Date.from(overdueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return recordsRepository.countByStatusAndBorrowDateBefore(0, overdueDateAsDate);
    }

    @Override
    public Page<RecordsDTO> getAllRecords(Pageable pageable) {
        return recordsRepository.findAll(pageable).map(RecordsMapper::toDTO);
    }

    @Override
    public long getLoansToday() {
        return recordsRepository.countRecordsByBorrowDate(LocalDate.now()); // 15/05/2025
    }

    @Override
    public Page<RecordsDTO> getOverdueRecordsPaged(Pageable pageable) {
        LocalDate today = LocalDate.now(); // 15/05/2025
        LocalDate overdueDate = today.minusDays(14);
        Date overdueDateAsDate = Date.from(overdueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return recordsRepository.findOverdueRecordsPaged(0, overdueDateAsDate, pageable)
                .map(RecordsMapper::toDTO);
    }
}