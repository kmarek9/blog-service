package pl.twojekursy.comment;

import lombok.Value;
import pl.twojekursy.post.Post;
import pl.twojekursy.post.PostScope;
import pl.twojekursy.post.PostStatus;

import java.time.LocalDateTime;

@Value
public class ReadCommentResponse {

     Long id;

    String text;

    LocalDateTime createdDateTime;

    String author;

    PostResponse post;

    public static ReadCommentResponse from(Comment comment){
        return new ReadCommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getCreatedDateTime(),
                comment.getAuthor(),
                PostResponse.from(comment.getPost())
        );
    }

    @Value
    static class PostResponse {

        Long id;

        Integer version;

        String text;

        LocalDateTime createdDateTime;

        PostScope scope;

        String author;

        LocalDateTime publicationDate;

        PostStatus status;

        public static PostResponse from(Post post) {
            return new PostResponse(
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
}
