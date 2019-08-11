package com.example.springdataes.Service;

import com.example.springdataes.Entity.BookBean;
import com.example.springdataes.Repository.BookRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("bookService")
public class BookServiceImpl implements BookService{

  @Autowired
  @Qualifier("bookRepository")
  private BookRepository bookRepository;

  @Override
  public Optional<BookBean> findById(String id) {
    return bookRepository.findById(id);
  }

  @Override
  public BookBean save(BookBean book) {
    return bookRepository.save(book);
  }

  @Override
  public void delete(BookBean book) {
    bookRepository.delete(book);
  }

  @Override
  public Optional<BookBean> findOne(String id) {
    return bookRepository.findById(id);
  }

  @Override
  public Page<BookBean> findAll() {
    return (Page<BookBean>) bookRepository.findAll();
  }

  @Override
  public Page<BookBean> findByAuthor(String author, PageRequest pageRequest) {
    return bookRepository.findByAuthor(author, pageRequest);
  }

  @Override
  public Page<BookBean> findByTitle(String title, PageRequest pageRequest) {
    return bookRepository.findByTitle(title, pageRequest);
  }

  @Override
  public Page<BookBean> search(QueryBuilder queryBuilder, Pageable pageable) {
    return bookRepository.search(queryBuilder, pageable);
  }
}
