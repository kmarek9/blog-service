package pl.twojekursy.user.settings;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class CreateUserSettingsRequest {

    @NotNull
    Long userId;

    @NotNull
    Boolean showPanel1;

    @NotNull
    Boolean darkMode;
}
