package pl.twojekursy.post;

import pl.twojekursy.comment.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record FindPostByGetMethodResponse(Long id, String text, LocalDateTime createdDateTime,
                                          String author) {

    public static FindPostByGetMethodResponse from(Post post) {
        return new FindPostByGetMethodResponse(
                post.getId(),
                post.getText(),
                post.getCreatedDateTime(),
                post.getAuthor()
        );
    }
}
