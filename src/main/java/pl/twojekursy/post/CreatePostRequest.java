package pl.twojekursy.post;

import java.time.LocalDateTime;

public class CreatePostRequest {

    private final String text;

    private final PostScope scope;

    private final String author;

    private final LocalDateTime publicationDate;

    public CreatePostRequest(String text, PostScope scope, String author, LocalDateTime publicationDate) {
        this.text = text;
        this.scope = scope;
        this.author = author;
        this.publicationDate = publicationDate;
    }

    public String getText() {
        return text;
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
}
