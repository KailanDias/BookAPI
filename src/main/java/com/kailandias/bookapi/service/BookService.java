package com.kailandias.bookapi.service;

import com.kailandias.bookapi.DTO.request.BookRequest;
import com.kailandias.bookapi.DTO.response.BookResponse;
import com.kailandias.bookapi.entity.Book;
import com.kailandias.bookapi.exception.BookNotFoundException;
import com.kailandias.bookapi.mapper.BookMapper;
import com.kailandias.bookapi.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository repository;
    private final BookMapper mapper;

    public BookService(BookRepository repository, BookMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public BookResponse create(BookRequest request) {
        Book saved = repository.save(mapper.toEntity(request));
        return mapper.toResponse(saved);
    }

    public List<BookResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public BookResponse findById(Long id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return mapper.toResponse(book);
    }

    public BookResponse update(Long id, BookRequest request) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setYearPublication(request.yearPublication());
        return mapper.toResponse(repository.save(book));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        repository.deleteById(id);
    }
}