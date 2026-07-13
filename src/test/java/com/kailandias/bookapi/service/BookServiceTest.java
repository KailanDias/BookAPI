package com.kailandias.bookapi.service;

import com.kailandias.bookapi.DTO.request.BookRequest;
import com.kailandias.bookapi.DTO.response.BookResponse;
import com.kailandias.bookapi.entity.Book;
import com.kailandias.bookapi.exception.BookNotFoundException;
import com.kailandias.bookapi.mapper.BookMapper;
import com.kailandias.bookapi.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock private BookRepository repository;
    @Mock private BookMapper mapper;
    @InjectMocks private BookService service;

    @Test
    void shouldCreateBook() {
        BookRequest request = new BookRequest("Clean Code", "Robert Martin", 2008);
        Book entity = new Book();
        Book saved = new Book();
        BookResponse response = new BookResponse(1L, "Clean Code", "Robert Martin", 2008);
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        assertEquals(response, service.create(request));
        verify(repository).save(entity);
    }

    @Test
    void shouldFindById() {
        Book book = new Book();
        BookResponse response = new BookResponse(1L, "Clean Code", "Robert Martin", 2008);
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(mapper.toResponse(book)).thenReturn(response);

        assertEquals(response, service.findById(1L));
    }

    @Test
    void shouldThrowWhenFindByIdMissing() {
        when(repository.findById(9999L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> service.findById(9999L));
    }

    @Test
    void shouldThrowWhenUpdateMissing() {
        BookRequest request = new BookRequest("New Title", "Author", 2020);
        when(repository.findById(9999L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> service.update(9999L, request));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldDeleteWhenExists() {
        when(repository.existsById(1L)).thenReturn(true);
        service.delete(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeleteMissing() {
        when(repository.existsById(9999L)).thenReturn(false);
        assertThrows(BookNotFoundException.class, () -> service.delete(9999L));
        verify(repository, never()).deleteById(any());
    }
}