package pl.twojekursy.client;

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
}

