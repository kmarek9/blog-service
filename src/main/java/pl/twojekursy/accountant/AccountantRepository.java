package pl.twojekursy.accountant;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountantRepository extends CrudRepository<Accountant, Long> {
    @Query("select a from Accountant a left join fetch a.clients where a.id=:accountantId")
    Optional<Accountant> findByIdFetchGroupsInfo(Long accountantId);
}
