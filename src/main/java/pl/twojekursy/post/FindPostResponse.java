package pl.twojekursy.post;

import pl.twojekursy.comment.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record FindPostResponse(Long id, String text, LocalDateTime createdDateTime,
                               String author, List<String> comments) {

    public static FindPostResponse from(Post post) {
        return new FindPostResponse(
                post.getId(),
                post.getText(),
                post.getCreatedDateTime(),
                post.getAuthor(),
                post.getComments().stream().map(Comment::getText).collect(Collectors.toList())
        );
    }
}
