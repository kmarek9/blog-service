package pl.twojekursy.accountant;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.client.Client;
import pl.twojekursy.client.ClientService;
import pl.twojekursy.groupinfo.GroupInfo;
import pl.twojekursy.user.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountantService {
    private final AccountantRepository accountantRepository;

    private final ClientService clientService;

    @Transactional
    public void create(CreateAccountantRequest accountantRequest){
        Accountant accountant = Accountant.builder()
                .name(accountantRequest.getName())
                .build();

        accountantRepository.save(accountant);
    }

    @Transactional
    public void attachClient(AttachClientRequest attachClientRequest) {
        Accountant accountant = accountantRepository.findByIdFetchGroupsInfo(attachClientRequest.getAccountantId())
                .orElseThrow(EntityNotFoundException::new);

        Client client = clientService.findById(attachClientRequest.getClientId());

        accountant.getClients().add(client);
    }
}

