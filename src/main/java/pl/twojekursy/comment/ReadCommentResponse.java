package pl.twojekursy.comment;

import lombok.Value;
import pl.twojekursy.post.ReadPostResponse;

import java.time.LocalDateTime;

@Value
public class ReadCommentResponse {

     Long id;

    String text;

    LocalDateTime createdDateTime;

    String author;

    ReadPostResponse post;

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
