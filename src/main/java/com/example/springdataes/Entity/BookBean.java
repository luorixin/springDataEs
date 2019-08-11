package com.example.springdataes.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "book", type = "_doc")
@Getter
@Setter
@ToString
public class BookBean {
  @Id
  private String id;
  private String title;
  private String author;
  private String postDate;

  public BookBean(){};

  public BookBean(String id, String title, String author, String postDate){
    this.id = id;
    this.title = title;
    this.author = author;
    this.postDate = postDate;
  }


}
