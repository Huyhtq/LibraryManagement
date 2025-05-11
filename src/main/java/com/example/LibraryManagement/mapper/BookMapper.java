package com.example.LibraryManagement.mapper;

import com.example.LibraryManagement.dto.BookDTO;
import com.example.LibraryManagement.entity.Book;

public class BookMapper {

    // Entity → DTO
    public static BookDTO toDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .quantity(book.getQuantity())
                .total(book.getTotal())
                .build();
    }

    // DTO → Entity
    public static Book toEntity(BookDTO dto) {
        return Book.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .quantity(dto.getQuantity())
                .total(dto.getTotal())
                .build();
    }
}