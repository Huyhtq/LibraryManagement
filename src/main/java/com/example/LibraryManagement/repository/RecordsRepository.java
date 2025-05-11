package com.example.LibraryManagement.repository;

import com.example.LibraryManagement.entity.Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.*;

@Repository
public interface RecordsRepository extends JpaRepository<Records,Long>{
    List<Records> findByBorrowerId(Long borrowerId);

    List<Records> findByBookId(Long bookId);

    List<Records> findByStatus(Integer status);

    @Query("SELECT r FROM Records r WHERE r.returnDate < CURRENT_DATE AND r.status = 0")
    List<Records> findOverdueRecords();
}
