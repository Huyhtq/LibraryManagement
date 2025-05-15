package com.example.LibraryManagement.service.impl;

import com.example.LibraryManagement.repository.BookRepository;
import com.example.LibraryManagement.dto.BookDTO;
import com.example.LibraryManagement.mapper.BookMapper;
import com.example.LibraryManagement.entity.Book;
import com.example.LibraryManagement.service.BookService;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.*;
import lombok.*;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long id) {
        return bookRepository.findById(id)
                .map(BookMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public BookDTO createBook(BookDTO dto) {
        Book book = BookMapper.toEntity(dto);
        return BookMapper.toDTO(bookRepository.save(book));
    }

    public BookDTO updateBook(Long id, BookDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setQuantity(dto.getQuantity());
        book.setTotal(dto.getTotal());
        return BookMapper.toDTO(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
