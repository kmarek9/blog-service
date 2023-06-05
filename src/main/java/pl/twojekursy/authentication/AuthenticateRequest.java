package pl.twojekursy.authentication;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
public class AuthenticateRequest {

    @NotBlank
    @Size(max = 100)
    String login;

    @NotBlank
    @Size(max = 30)
    String password;
}
