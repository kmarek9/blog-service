package pl.twojekursy.post;

import lombok.Value;
import pl.twojekursy.comment.Comment;
import pl.twojekursy.comment.ReadCommentResponse;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class ReadPostResponse {

    Long id;

    Integer version;

    String text;

    LocalDateTime createdDateTime;

    PostScope scope;

    String author;

    LocalDateTime publicationDate;

    PostStatus status;

    List<CommentResponse> comments;

    public static ReadPostResponse from(Post post){
        return new ReadPostResponse(
                post.getId(),
                post.getVersion(),
                post.getText(),
                post.getCreatedDateTime(),
                post.getScope(),
                post.getAuthor(),
                post.getPublicationDate(),
                post.getStatus(),
                post.getComments().stream()
                        .map(CommentResponse::from)
                        .sorted(Comparator.comparing(CommentResponse::getCreatedDateTime).reversed())
                        .collect(Collectors.toList())
        );
    }


    @Value
    public static class CommentResponse {

        Long id;

        String text;

        LocalDateTime createdDateTime;

        String author;

        public static CommentResponse from(Comment comment){
            return new CommentResponse(
                    comment.getId(),
                    comment.getText(),
                    comment.getCreatedDateTime(),
                    comment.getAuthor()
            );
        }
    }
}
