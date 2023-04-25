package pl.twojekursy.comment;

import java.time.LocalDateTime;

public class ReadCommentResponse {

    private final Long id;

    private final String text;

    private final LocalDateTime createdDateTime;

    private final String author;

    private final Long postId;

    public ReadCommentResponse(Long id, String text, LocalDateTime createdDateTime, String author, Long postId) {
        this.id = id;
        this.text = text;
        this.createdDateTime = createdDateTime;
        this.author = author;
        this.postId = postId;
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

    public Long getPostId() {
        return postId;
    }

    public static ReadCommentResponse from(Comment comment){
        return new ReadCommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getCreatedDateTime(),
                comment.getAuthor(),
                comment.getPost().getId()
        );
    }
}
