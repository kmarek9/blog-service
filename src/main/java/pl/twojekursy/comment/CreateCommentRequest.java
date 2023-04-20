package pl.twojekursy.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateCommentRequest {

    @NotBlank
    @Size(max = 5000)
    private final String text;

    @NotBlank
    @Size(max = 100)
    private final String author;

    @NotNull
    private final Long postId;

    public CreateCommentRequest(String text, String author, Long postId) {
        this.text = text;
        this.author = author;
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public Long getPostId() {
        return postId;
    }
}
