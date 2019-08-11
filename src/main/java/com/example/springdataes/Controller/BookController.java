package com.example.springdataes.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.springdataes.Entity.BookBean;
import com.example.springdataes.Service.BookService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
public class BookController {
  @Autowired
  private BookService bookService;

  @Resource
  private ElasticsearchTemplate elasticsearchTemplate;

  @GetMapping("/{id}")
  public BookBean getBookById(@PathVariable String id){
    Optional<BookBean> opt = bookService.findById(id);
    BookBean book = opt.get();
    return book;
  }

  @GetMapping("/all")
  public Page<BookBean> getBookAll(){
    Page<BookBean> all = bookService.findAll();
    return all;
  }

  @PostMapping("/save")
  public void save(@RequestBody BookBean bookBean){
    bookService.save(bookBean);
  }

  @GetMapping("search")
  public String search(@RequestParam("keyword") String keyword){
    BoolQueryBuilder queryBuilder = getQueryBuild(keyword);
    Pageable pageable = PageRequest.of(0, 10);
    Iterable<BookBean> bookBeans = bookService.search(queryBuilder, pageable);
    return JSONObject.toJSONString(bookBeans);
  }

  @GetMapping("hsearch")
  public String hsearch(@RequestParam("keyword") String keyword){
    BoolQueryBuilder queryBuilder = getQueryBuild(keyword);
    Pageable pageable = PageRequest.of(0, 10);
    String preTag = "<b color='red'>";
    String postTag = "</b>";
    SearchQuery searchQuery = new NativeSearchQueryBuilder()
                              .withQuery(queryBuilder)
                              .withHighlightFields(new HighlightBuilder.Field("author").preTags(preTag).postTags(postTag),new HighlightBuilder.Field("title").preTags(preTag).postTags(postTag))
                              .withPageable(pageable)
                              .build();
    AggregatedPage<BookBean> bookBeans =elasticsearchTemplate.queryForPage(searchQuery, BookBean.class, new SearchResultMapper() {
      @Override
      public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
        List<BookBean> list = new ArrayList<BookBean>();
        for(SearchHit hit: searchResponse.getHits()){
          BookBean bookBean = new BookBean();
          HighlightField author = hit.getHighlightFields().get("author");
          if (author != null){
            bookBean.setAuthor(author.getFragments()[0].string());
          }else{
            bookBean.setAuthor(hit.getSourceAsMap().get("author").toString());
          }
          HighlightField title = hit.getHighlightFields().get("title");
          if (title != null){
            bookBean.setTitle(title.getFragments()[0].string());
          }else{
            bookBean.setTitle(hit.getSourceAsMap().get("title").toString());
          }
          list.add(bookBean);
        }
        return new AggregatedPageImpl<>((List<T>)list);
      }
    });
    return JSONObject.toJSONString(bookBeans);
  }

  private BoolQueryBuilder getQueryBuild(String keyword){
    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    queryBuilder.should(QueryBuilders.matchQuery("author", keyword));
    queryBuilder.should(QueryBuilders.matchQuery("title", keyword));

    return  queryBuilder;
  }
}
