package pl.twojekursy.user;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import pl.twojekursy.BaseUnitTest;
import pl.twojekursy.groupinfo.GroupInfo;
import pl.twojekursy.groupinfo.GroupInfoService;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
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

    @Test
    void givenUserIdNotExist_whenJoinToGroup_thenEntityNotFoundException() {
        // given
        JoinToGroupRequest request = new JoinToGroupRequest(45L, 23L);
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.empty());

        // when
        Executable executable = () -> underTest.joinToGroup(request);

        // then
        Assertions.assertThrows(EntityNotFoundException.class, executable);
        verify(userRepository).findById(request.getUserId());
        verifyNoInteractions(groupInfoService);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void givenGroupIdNotExist_whenJoinToGroup_thenEntityNotFoundException() {
        // given
        JoinToGroupRequest request = new JoinToGroupRequest(45L, 23L);
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(User.builder()
                .groupsInfo(new HashSet<>())
                .build()));
        when(groupInfoService.findById(request.getGroupId())).thenThrow(EntityNotFoundException.class);

        // when
        Executable executable = () -> underTest.joinToGroup(request);

        // then
        Assertions.assertThrows(EntityNotFoundException.class, executable);
        verify(userRepository).findById(request.getUserId());
        verify(groupInfoService).findById(request.getGroupId());

        verifyNoMoreInteractions(userRepository, groupInfoService);
    }

    @Test
    void givenCorrectRequest_whenJoinToGroup_thenAddGroupToUser() {
        // given
        JoinToGroupRequest request = new JoinToGroupRequest(45L, 23L);
        User user = User.builder()
                .id(request.getUserId())
                .groupsInfo(new HashSet<>())
                .build();

        GroupInfo groupInfo = GroupInfo.builder()
                .id(request.getGroupId())
                .build();

        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(user));
        when(groupInfoService.findById(request.getGroupId())).thenReturn(groupInfo);

        // when
        underTest.joinToGroup(request);

        // then
        assertThat(user.getGroupsInfo())
                .hasSize(1)
                .containsExactly(groupInfo)
        ;

        verify(userRepository).findById(request.getUserId());
        verify(groupInfoService).findById(request.getGroupId());
    }
}