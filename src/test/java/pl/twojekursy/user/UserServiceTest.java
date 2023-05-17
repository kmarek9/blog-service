package pl.twojekursy.user;

import org.junit.jupiter.api.Test;
import org.mockito.*;
import pl.twojekursy.BaseUnitTest;
import pl.twojekursy.groupinfo.GroupInfoService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;


class UserServiceTest extends BaseUnitTest {
    @InjectMocks
    private UserService underTest;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupInfoService groupInfoService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void givenCorrectRequest_whenCreate_thenCreateUser() {
        // given
        CreateUserRequest createUserRequest = new CreateUserRequest("login");

        // when
        underTest.create(createUserRequest);

        // then
        verify(userRepository).save(userCaptor.capture());

        User user = userCaptor.getValue();
        assertThat(user).isNotNull();
        assertThat(user.getLogin()).isEqualTo(createUserRequest.getLogin());
        assertThat(user.getId()).isNull();
        assertThat(user.getVersion()).isNull();
        assertThat(user.getCreatedDateTime()).isNull();
        assertThat(user.getLastModifiedDateTime()).isNull();
        assertThat(user.getGroupsInfo()).isNull();
        assertThat(user.getAddress()).isNull();
    }
}