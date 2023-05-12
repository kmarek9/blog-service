package pl.twojekursy.client;

import jakarta.validation.constraints.NotNull;

public record FindClientRequest(@NotNull Long accountantId,
                                String name) {
}
