package pl.twojekursy.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateCommentRequest {

    @NotBlank
    @Size(max = 5000)
    private final String text;

    @NotBlank
    @Size(max = 100)
    private final String author;

    public UpdateCommentRequest(String text, String author) {
        this.text = text;
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }
}
