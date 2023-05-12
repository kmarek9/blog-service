package pl.twojekursy.user;

import jakarta.validation.constraints.NotNull;

public record FindUserRequest(@NotNull Long groupId,
                              String login) {
}
