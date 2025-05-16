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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class RecordsServiceImpl implements RecordsService {

    private final RecordsRepository recordsRepository;
    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;

    @Override
    public Page<RecordsDTO> getAllRecords(Pageable pageable, String search, Integer status) {
        if (search != null && !search.isEmpty()) {
            return recordsRepository.findBySearchTerm(search.toLowerCase(), status, pageable)
                    .map(RecordsMapper::toDTO);
        }
        if (status != null) {
            return recordsRepository.findByStatus(status, pageable)
                    .map(RecordsMapper::toDTO);
        }
        return recordsRepository.findAllWithDetails(pageable).map(RecordsMapper::toDTO);
    }

    @Override
    @Transactional
    public void borrowBook(Long bookId, Long borrowerId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new IllegalArgumentException("Borrower not found"));

        if (book.getQuantity() <= 0) {
            throw new IllegalStateException("Book is out of stock");
        }

        Records record = Records.builder()
                .book(book)
                .borrower(borrower)
                .borrowDate(new Date())
                .status(0)
                .build();

        book.setQuantity(book.getQuantity() - 1);
        borrower.setBorrowCount(borrower.getBorrowCount() + 1);

        recordsRepository.save(record);
        bookRepository.save(book);
        borrowerRepository.save(borrower);
    }

    @Override
    @Transactional
    public void returnBook(Long id) {
        Records record = recordsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record with ID " + id + " not found"));

        if (record.getStatus() == 1) {
            throw new IllegalStateException("Book has already been returned");
        }

        record.setStatus(1);
        record.setReturnDate(new Date());

        Book book = record.getBook();
        book.setQuantity(book.getQuantity() + 1);

        Borrower borrower = record.getBorrower();
        borrower.setBorrowCount(borrower.getBorrowCount() - 1);

        recordsRepository.save(record);
        bookRepository.save(book);
        borrowerRepository.save(borrower);
    }

    @Override
    public Page<RecordsDTO> getOverdueRecordsPaged(Pageable pageable) {
        LocalDate today = LocalDate.now();
        LocalDate overdueDate = today.minusDays(14);
        return recordsRepository.findByStatusAndBorrowDateBefore(0, overdueDate, pageable)
                .map(RecordsMapper::toDTO);
    }

    @Override
    public long getLoansToday() {
        return recordsRepository.countByBorrowDate(LocalDate.now());
    }
}