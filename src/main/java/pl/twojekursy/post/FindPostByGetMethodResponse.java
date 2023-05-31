package pl.twojekursy.post;

import java.time.LocalDateTime;

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
