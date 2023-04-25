package pl.twojekursy.comment;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CommentRepository  extends CrudRepository<Comment, Long> {
    @Query("select c from Comment c join c.post where c.id=:id")
    Optional<Comment> findByIdFetchPost(Long id);
}
