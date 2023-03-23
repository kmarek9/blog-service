package pl.twojekursy.post;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class UpdatePostRequest {

    @NotBlank
    @Size(max = 5000)
    private final String text;

    @NotNull
    private final PostScope scope;

    public UpdatePostRequest(String text, PostScope scope) {
        this.text = text;
        this.scope = scope;
    }

    public String getText() {
        return text;
    }

    public PostScope getScope() {
        return scope;
    }
}
