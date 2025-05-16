package com.example.LibraryManagement.repository;

import com.example.LibraryManagement.entity.Records;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RecordsRepository extends JpaRepository<Records, Long> {

    @Query("SELECT r FROM Records r WHERE r.status = :status AND r.borrowDate < :date")
    Page<Records> findByStatusAndBorrowDateBefore(int status, LocalDate date, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Records r WHERE DATE(r.borrowDate) = :date")
    long countByBorrowDate(LocalDate date);

    @Query("SELECT r FROM Records r JOIN FETCH r.book JOIN FETCH r.borrower")
    Page<Records> findAllWithDetails(Pageable pageable);

    @Query("SELECT r FROM Records r JOIN FETCH r.book JOIN FETCH r.borrower WHERE r.status = :status")
    Page<Records> findByStatus(int status, Pageable pageable);

    @Query("SELECT r FROM Records r JOIN FETCH r.book b JOIN FETCH r.borrower br " +
           "WHERE (LOWER(br.name) LIKE %:search% OR LOWER(b.title) LIKE %:search%) " +
           "AND (:status IS NULL OR r.status = :status)")
    Page<Records> findBySearchTerm(String search, Integer status, Pageable pageable);
}