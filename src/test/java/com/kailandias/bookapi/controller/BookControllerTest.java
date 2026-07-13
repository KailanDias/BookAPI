package com.kailandias.bookapi.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kailandias.bookapi.DTO.request.BookRequest;
import com.kailandias.bookapi.DTO.response.BookResponse;
import com.kailandias.bookapi.exception.BookNotFoundException;
import com.kailandias.bookapi.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;   // Spring Boot 4: pacote novo
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private BookService service;

    @Test
    void shouldCreateBookAndReturnLocation() throws Exception {
        BookRequest request = new BookRequest("Clean Code", "Robert Martin", 2008);
        when(service.create(any()))
                .thenReturn(new BookResponse(1L, "Clean Code", "Robert Martin", 2008));

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/books/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    void shouldReturnBadRequestWhenInvalid() throws Exception {
        BookRequest invalid = new BookRequest("", "", -5);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.title").exists())
                .andExpect(jsonPath("$.errors.author").exists())
                .andExpect(jsonPath("$.errors.yearPublication").exists());

        verify(service, never()).create(any());
    }

    @Test
    void shouldListBooks() throws Exception {
        when(service.findAll()).thenReturn(List.of(
                new BookResponse(1L, "Clean Code", "Robert Martin", 2008)));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Clean Code"));
    }

    @Test
    void shouldFindById() throws Exception {
        when(service.findById(1L))
                .thenReturn(new BookResponse(1L, "Clean Code", "Robert Martin", 2008));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    void shouldReturnNotFoundWhenBookMissing() throws Exception {
        when(service.findById(9999L)).thenThrow(new BookNotFoundException(9999L));

        mockMvc.perform(get("/api/books/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Book not found with id: 9999"));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        BookRequest request = new BookRequest("New Title", "Author", 2020);
        when(service.update(eq(1L), any()))
                .thenReturn(new BookResponse(1L, "New Title", "Author", 2020));

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.yearPublication").value(2020));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }
}