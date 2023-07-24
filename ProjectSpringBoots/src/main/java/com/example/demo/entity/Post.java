package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "post")
@Table(name = "post")
//@NamedNativeQuery(
//        name = "findAllByStatus",
//        query= "SELECT * FROM user WHERE id = ?1",
//        result
//)
public class Post {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "title",nullable = false,length = 300)
    private String title;
    @Column(name = "slug",nullable = false,length = 300)
    private String slug;
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "thumbnail",length = 255)
    private String thumbnail;
    @Column(name = "status", columnDefinition = "int default 0")
    private int status;
    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @Column(name = "published_at")
    private Timestamp publishedAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    private User modifiedBy;
}
