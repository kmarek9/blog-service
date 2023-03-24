package pl.twojekursy.post;

import java.time.LocalDateTime;

public class ReadPostResponse {

    private final Long id;

    private final Integer version;

    private final String text;

    private final LocalDateTime createdDateTime;

    private final PostScope scope;

    private final String author;

    private final LocalDateTime publicationDate;

    private final PostStatus status;



    public ReadPostResponse(Long id, Integer version, String text, LocalDateTime createdDateTime, PostScope scope, String author, LocalDateTime publicationDate, PostStatus status) {
        this.id = id;
        this.version = version;
        this.text = text;
        this.createdDateTime = createdDateTime;
        this.scope = scope;
        this.author = author;
        this.publicationDate = publicationDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
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

    public static ReadPostResponse from(Post post){
        return new ReadPostResponse(
                post.getId(),
                post.getVersion(),
                post.getText(),
                post.getCreatedDateTime(),
                post.getScope(),
                post.getAuthor(),
                post.getPublicationDate(),
                post.getStatus()
        );
    }
}
