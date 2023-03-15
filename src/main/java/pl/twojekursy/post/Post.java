package pl.twojekursy.post;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    private PostScope scope;

    private String author;

    private LocalDateTime publicationDate;

    private PostStatus status;

}
