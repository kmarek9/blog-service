package pl.twojekursy.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    @Query("select u from User u join u.groupsInfo gi where gi.id = :groupId")
    Page<User> find(Long groupId, PageRequest login);
}
