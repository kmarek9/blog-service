package pl.twojekursy.post;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @NotNull
    private Integer version;

    @NotBlank
    @NotNull
    @Size(max = 5000)
    private String text;

    @NotNull
    private LocalDateTime createdDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PostScope scope;

    @NotBlank
    @NotNull
    @Size(max = 100)
    private String author;

    @FutureOrPresent
    private LocalDateTime publicationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    public Post(){

    }

    public Post(Post old){
        this.id = old.id;
        this.version = old.version;
        this.text = old.text;
        this.createdDate = old.createdDate;
        this.scope = old.scope;
        this.author = old.author;
        this.publicationDate = old.publicationDate;
        this.status = old.status;
    }

    public Post(String text, PostScope scope, String author, LocalDateTime publicationDate) {
        this.text = text;
        this.createdDate = LocalDateTime.now();
        this.scope = scope;
        this.author = author;
        this.publicationDate = publicationDate;
        this.status = PostStatus.ACTIVE;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setScope(PostScope scope) {
        this.scope = scope;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public PostScope getScope() {
        return scope;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public PostStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", createdDate=" + createdDate +
                ", scope=" + scope +
                ", author='" + author + '\'' +
                ", publicationDate=" + publicationDate +
                ", status=" + status +
                '}';
    }
}
