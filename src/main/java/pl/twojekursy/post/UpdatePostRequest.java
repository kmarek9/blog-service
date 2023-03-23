package pl.twojekursy.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdatePostRequest {

    @NotNull
    private final Integer version;

    @NotBlank
    @Size(max = 5000)
    private final String text;

    @NotNull
    private final PostScope scope;

    public UpdatePostRequest(Integer version, String text, PostScope scope) {
        this.version = version;
        this.text = text;
        this.scope = scope;
    }

    public Integer getVersion() {
        return version;
    }

    public String getText() {
        return text;
    }

    public PostScope getScope() {
        return scope;
    }
}
