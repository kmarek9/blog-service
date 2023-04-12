package pl.twojekursy.post;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findByStatusOrderByCreatedDateTimeDesc(PostStatus postStatus);

    List<Post> findByStatusOrderByCreatedDateTime(PostStatus postStatus);

    List<Post> findByStatus(PostStatus postStatus, Sort sort);

    List<Post> findByStatusAndAuthor(PostStatus postStatus, String author);

    List<Post> findByStatusInAndAuthorLike(Set<PostStatus> postStatuses, String author);

    List<Post> findByStatusInAndAuthorContaining(Set<PostStatus> postStatuses, String author);
    List<Post> findByStatusInAndAuthorStartingWith(Set<PostStatus> postStatuses, String author);

    List<Post> findByStatusInAndCreatedDateTimeBetween(Set<PostStatus> postStatuses, LocalDateTime startDate, LocalDateTime endDate);

    long countByStatus(PostStatus postStatus);

    boolean existsByStatus(PostStatus postStatus);
}
