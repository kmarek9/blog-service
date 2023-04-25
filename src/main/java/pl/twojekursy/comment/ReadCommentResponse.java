package pl.twojekursy.comment;

import pl.twojekursy.post.ReadPostResponse;

import java.time.LocalDateTime;

public class ReadCommentResponse {

    private final Long id;

    private final String text;

    private final LocalDateTime createdDateTime;

    private final String author;

    private final ReadPostResponse post;

    public ReadCommentResponse(Long id, String text, LocalDateTime createdDateTime, String author, ReadPostResponse post) {
        this.id = id;
        this.text = text;
        this.createdDateTime = createdDateTime;
        this.author = author;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public String getAuthor() {
        return author;
    }

    public ReadPostResponse getPost() {
        return post;
    }

    public static ReadCommentResponse from(Comment comment){
        return new ReadCommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getCreatedDateTime(),
                comment.getAuthor(),
                ReadPostResponse.from(comment.getPost())
        );
    }
}
