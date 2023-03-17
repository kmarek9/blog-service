package pl.twojekursy.invoice;

import java.time.LocalDate;

public record CreateInvoiceRequest(LocalDate paymentDate, String buyer, String seller) {
}
