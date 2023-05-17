package pl.twojekursy.accountant;

import org.junit.jupiter.api.Test;
import org.mockito.*;
import pl.twojekursy.BaseUnitTest;
import pl.twojekursy.client.ClientService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AccountantServiceTest extends BaseUnitTest {

    @InjectMocks
    private AccountantService underTest;

    @Mock
    private AccountantRepository accountantRepository;

    @Mock
    private ClientService clientService;

    @Captor
    private ArgumentCaptor<Accountant> accountantCaptor;

    @Test
    void givenCorrectRequest_whenCreate_thenCreateAccountant() {
        //given
        CreateAccountantRequest request = new CreateAccountantRequest("Marek K");

        //when
        underTest.create(request);

        //then
        Mockito.verify(accountantRepository).save(accountantCaptor.capture());

        Accountant accountant = accountantCaptor.getValue();
        assertThat(accountant).isNotNull();
        assertThat(accountant.getName()).isEqualTo(request.getName());
        assertThat(accountant.getId()).isNull();
        assertThat(accountant.getVersion()).isNull();
        assertThat(accountant.getCreatedDateTime()).isNull();
        assertThat(accountant.getLastModifiedDateTime()).isNull();
        assertThat(accountant.getClients()).isNull();
    }
}