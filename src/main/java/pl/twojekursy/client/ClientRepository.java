package pl.twojekursy.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Long> {
    @Query("select c from Client c join c.accountants a where a.id = :accountantId")
    Page<Client> find(Long accountantId, Pageable pageable);
}
