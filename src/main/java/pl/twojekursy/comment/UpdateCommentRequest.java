package pl.twojekursy.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class UpdateCommentRequest {

    @NotBlank
    @Size(max = 5000)
    String text;

    @NotBlank
    @Size(max = 100)
    String author;
}
