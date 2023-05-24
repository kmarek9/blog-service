package pl.twojekursy.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FindUserResponseTest {

    @Test
    void givenUser_whenFrom_thenCorrectResponse() {
        // given
        long expectedId = 67L;
        String expectedLogin = "login";

        User user = User.builder()
                .id(expectedId)
                .login(expectedLogin)
                .build();

        // when
        FindUserResponse response = FindUserResponse.from(user);

        // then
        /*Assertions.assertNotNull(response);
        Assertions.assertEquals(expectedId, response.getId());
        Assertions.assertEquals(expectedLogin, response.getLogin());*/
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(expectedId);
        assertThat(response.getLogin()).isEqualTo(expectedLogin);
    }
}