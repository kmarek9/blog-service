package pl.twojekursy.post;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CreatePostRequest {

    @NotBlank
    @Size(max = 5000)
    private final String text;

    @NotNull
    private final PostScope scope;

    @NotBlank
    @Size(max = 100)
    private final String author;

    @FutureOrPresent
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
