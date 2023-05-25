package pl.twojekursy.invoice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateInvoiceRequest(@NotNull Integer version,
                                   @NotNull LocalDate paymentDate,
                                   @NotBlank @Size(max = 150) String buyer,
                                   @NotBlank @Size(max = 150) String seller) {
}
