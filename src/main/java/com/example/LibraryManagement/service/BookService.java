package com.example.LibraryManagement.service;

import com.example.LibraryManagement.dto.BookDTO;
import java.util.*;

public interface BookService {
    List<BookDTO> getAllBooks();
    BookDTO getBookById(Long id);
    BookDTO createBook(BookDTO bookDTO);
    BookDTO updateBook(Long id, BookDTO bookDTO);
    void deleteBook(Long id);
}
