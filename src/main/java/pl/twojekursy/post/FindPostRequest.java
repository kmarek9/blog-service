package pl.twojekursy.post;

import java.time.LocalDateTime;
import java.util.Set;

public record FindPostRequest(Set<PostStatus> postStatuses,
                              String text,
                              LocalDateTime publicationDate,
                              LocalDateTime createdDateTimeMin,
                              LocalDateTime createdDateTimeMax) {
}
