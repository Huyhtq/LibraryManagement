package com.example.LibraryManagement.repository;

import com.example.LibraryManagement.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower,Long>{
    Optional<Borrower> findByEmail(String email);

    List<Borrower> findByBorrowCountGreaterThan(int minBorrowCount);
}
