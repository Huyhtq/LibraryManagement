package com.example.LibraryManagement.repository;

import com.example.LibraryManagement.entity.Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.*;

@Repository
public interface RecordsRepository extends JpaRepository<Records,Long>{
    List<Records> findByBorrowerId(Long borrowerId);

    List<Records> findByBookId(Long bookId);

    List<Records> findByStatus(Integer status);

    @Query("SELECT r FROM Records r WHERE r.return_date < CURRENT_DATE AND r.status = 0")
    List<Records> findOverdueRecords();

    @Query("SELECT COUNT(r) FROM Records r WHERE r.status = :status AND r.borrow_date < :date")
    long countByStatusAndBorrowDateBefore(int status, Date date);

    // Count records borrowed today (15/05/2025)
    @Query("SELECT COUNT(r) FROM Records r WHERE DATE(r.borrow_date) = :date")
    long countRecordsByBorrowDate(LocalDate date);

    @Query("SELECT r FROM Records r WHERE DATE(r.borrow_date) = :borrowDate")
    List<Records> findByBorrowDate(LocalDate borrowDate);

    @Query("SELECT r FROM Records r WHERE DATE(r.return_date) = :returnDate")
    List<Records> findByReturnDate(LocalDate returnDate);

    // Find records by book ID and status
    List<Records> findByBookIdAndStatus(Long bookId, Integer status);

    // Find records by borrower ID and status
    List<Records> findByBorrowerIdAndStatus(Long borrowerId, Integer status);

    // Custom query to get records with book and borrower details
    @Query("SELECT r FROM Records r JOIN FETCH r.book b JOIN FETCH r.borrower br WHERE r.status = :status")
    List<Records> findRecordsWithDetails(Integer status);

    // Find overdue records with pagination (optional)
    @Query("SELECT r FROM Records r WHERE r.status = :status AND r.borrow_date < :date")
    Page<Records> findOverdueRecordsPaged(int status, Date date, Pageable pageable);

    // New method to count overdue records based on borrow_date
    @Query("SELECT COUNT(r) FROM Records r WHERE r.borrow_date < :overdueDate AND r.status = 0")
    long countByStatusAndBorrow_dateBefore(Integer status, LocalDate overdueDate);
    
    @Query("SELECT r FROM Records r JOIN FETCH r.book JOIN FETCH r.borrower")
    List<Records> findAllWithDetails();
}
