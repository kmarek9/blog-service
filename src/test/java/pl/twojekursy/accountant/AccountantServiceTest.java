package pl.twojekursy.accountant;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.twojekursy.BaseUnitTest;
import pl.twojekursy.client.Client;
import pl.twojekursy.client.ClientService;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        verify(accountantRepository).save(accountantCaptor.capture());

        Accountant accountant = accountantCaptor.getValue();
        assertThat(accountant).isNotNull();
        assertThat(accountant.getName()).isEqualTo(request.getName());
        assertThat(accountant.getId()).isNull();
        assertThat(accountant.getVersion()).isNull();
        assertThat(accountant.getCreatedDateTime()).isNull();
        assertThat(accountant.getLastModifiedDateTime()).isNull();
        assertThat(accountant.getClients()).isNull();
    }

    @Test
    void givenAccountantIdNotExist_whenAttachClient_thenEntityNotFoundException() {
        // given
        AttachClientRequest request = new AttachClientRequest(123L, 5463L);
        when(accountantRepository.findByIdFetchGroupsInfo(request.getAccountantId())).thenReturn(Optional.empty());

        // when
        Executable executable = () -> underTest.attachClient(request);

        // then
        Assertions.assertThrows(EntityNotFoundException.class, executable);
        verifyNoInteractions(clientService);
    }

    @Test
    void givenClientIdNotExist_whenAttachClient_thenEntityNotFoundException() {
        // given
        AttachClientRequest request = new AttachClientRequest(123L, 5463L);
        when(accountantRepository.findByIdFetchGroupsInfo(request.getAccountantId()))
                .thenReturn(Optional.of(mock(Accountant.class)));

        when(clientService.findById(request.getClientId()))
                .thenThrow(IllegalArgumentException.class);

        // when
        Executable executable = () -> underTest.attachClient(request);

        // then
        Assertions.assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void givenCorrectRequest_whenAttachClient_thenAttachClientToAccountant() {
        // given
        AttachClientRequest request = new AttachClientRequest(123L, 5463L);
        Accountant accountant = Accountant.builder()
                .id(request.getAccountantId())
                .clients(new HashSet<>())
                .build();
        Client client = Client.builder()
                .id(request.getClientId())
                .build();

        when(accountantRepository.findByIdFetchGroupsInfo(request.getAccountantId()))
                .thenReturn(Optional.of(accountant));
        when(clientService.findById(request.getClientId()))
                .thenReturn(client);

        // when
        underTest.attachClient(request);

        // then
        assertThat(accountant.getClients())
                .hasSize(1)
                .containsExactlyInAnyOrder(client);
    }
}