package com.kailandias.bookapi.service;

import com.kailandias.bookapi.DTO.request.BookRequest;
import com.kailandias.bookapi.DTO.response.BookResponse;
import com.kailandias.bookapi.exception.BookNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class BookServiceIntegrationTest {

    @Autowired
    private BookService service;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldCreateBook() {
        BookRequest request = new BookRequest("Clean Code", "Robert Martin", 2008);

        BookResponse created = service.create(request);

        assertNotNull(created.id());
        assertEquals("Clean Code", created.title());
        assertEquals("Robert Martin", created.author());
        assertEquals(2008, created.yearPublication());
    }

    @Test
    void shouldListBooks() {
        service.create(new BookRequest("Domain-Driven Design", "Eric Evans", 2003));

        List<BookResponse> books = service.findAll();

        assertFalse(books.isEmpty());
    }

    @Test
    void shouldUpdateBook() {
        BookResponse created = service.create(new BookRequest("Old Title", "Author", 2000));

        BookResponse updated = service.update(
                created.id(),
                new BookRequest("New Title", "Author", 2020));

        assertEquals(created.id(), updated.id());
        assertEquals("New Title", updated.title());
        assertEquals(2020, updated.yearPublication());
    }

    @Test
    void shouldDeleteBook() {
        BookResponse created = service.create(new BookRequest("To Delete", "Author", 1999));
        Long id = created.id();

        service.delete(id);

        assertThrows(BookNotFoundException.class, () -> service.findById(id));
    }

    @Test
    void shouldThrowWhenBookNotFound() {
        assertThrows(BookNotFoundException.class, () -> service.findById(9999L));
    }
}
