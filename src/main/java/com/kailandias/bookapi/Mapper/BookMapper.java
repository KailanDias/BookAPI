package com.kailandias.bookapi.Mapper;

import com.kailandias.bookapi.DTO.BookRequest;
import com.kailandias.bookapi.DTO.BookResponse;
import com.kailandias.bookapi.Entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toEntity(BookRequest request);

    BookResponse toResponse(Book book);
}
