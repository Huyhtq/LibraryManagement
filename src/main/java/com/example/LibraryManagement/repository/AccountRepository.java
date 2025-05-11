package com.example.LibraryManagement.repository;

import com.example.LibraryManagement.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long>{
    Optional<Account> findByUsername(String username);
    Optional<Account> findByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
}
