package com.example.springdataes.Service;

import com.example.springdataes.Entity.BookBean;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {
  Optional<BookBean> findById(String id);

  BookBean save(BookBean book);

  void  delete(BookBean book);

  Optional<BookBean> findOne(String id);

  Page<BookBean> findAll();

  Page<BookBean> findByAuthor(String author, PageRequest pageRequest);

  Page<BookBean> findByTitle(String title, PageRequest pageRequest);

  Page<BookBean> search(QueryBuilder queryBuilder, Pageable pageable);
}
