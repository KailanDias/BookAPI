package com.kailandias.bookapi.mapper;

import com.kailandias.bookapi.DTO.request.BookRequest;
import com.kailandias.bookapi.DTO.response.BookResponse;
import com.kailandias.bookapi.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toEntity(BookRequest request);

    BookResponse toResponse(Book book);
}
