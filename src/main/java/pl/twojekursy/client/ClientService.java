package pl.twojekursy.client;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}

