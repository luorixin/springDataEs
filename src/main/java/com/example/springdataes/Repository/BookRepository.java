package com.example.springdataes.Repository;

import com.example.springdataes.Entity.BookBean;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 接口关系：
 * ElasticsearchRepository --> ElasticsearchCrudRepository --> PagingAndSortingRepository --> CrudRepository
 */
public interface BookRepository extends ElasticsearchRepository<BookBean, String> {

  Page<BookBean> findByAuthor(String author, Pageable pageable);

  Page<BookBean> findByTitle(String title, Pageable pageable);

}
