package pl.twojekursy.accountant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountantService {
    private final AccountantRepository accountantRepository;

    @Transactional
    public void create(CreateAccountantRequest accountantRequest){
        Accountant accountant = Accountant.builder()
                .name(accountantRequest.getName())
                .build();

        accountantRepository.save(accountant);
    }
}

