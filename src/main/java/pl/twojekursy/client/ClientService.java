package pl.twojekursy.client;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.accountant.Accountant;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;

    @Transactional
    public void create(CreateClientRequest clientRequest){
        Client client = Client.builder()
                .name(clientRequest.getName())
                .build();

        clientRepository.save(client);
    }

    public Client findById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Page<FindClientResponse> find(Long accountantId, Pageable pageable) {
        return clientRepository.find(accountantId, pageable)
                .map(FindClientResponse::from);
    }

    public Page<FindClientResponse> find(FindClientRequest findClientRequest, Pageable pageable) {
        Page<Client> users = clientRepository.findAll(prepareSpec(findClientRequest), pageable);
        return users.map(FindClientResponse::from);
    }

    private Specification<Client> prepareSpec(FindClientRequest findClientRequest) {
        return (root, query, criteriaBuilder) -> {
            Join<Client, Accountant> joinAccountants = root.join("accountants");

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(joinAccountants.get("id"), findClientRequest.accountantId()));

            if(findClientRequest.name()!=null ) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + findClientRequest.name() + "%"));
            }

            return criteriaBuilder.and( predicates.toArray(new Predicate[0]));
        };
    }
}

