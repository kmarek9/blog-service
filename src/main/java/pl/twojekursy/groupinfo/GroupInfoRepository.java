package pl.twojekursy.groupinfo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GroupInfoRepository extends CrudRepository<GroupInfo, Long> {
    @Query("select g from GroupInfo g join g.users u where u.id = :userId")
    Page<GroupInfo> find(Long userId, Pageable pageable);
}
