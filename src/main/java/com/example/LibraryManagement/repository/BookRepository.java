package com.example.LibraryManagement.repository;

import com.example.LibraryManagement.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.*;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>{
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);

    @Query("SELECT b FROM Book b WHERE b.quantity > 0")
    List<Book> findAvailableBooks();
}
