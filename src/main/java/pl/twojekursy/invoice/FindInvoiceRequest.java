package pl.twojekursy.invoice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record FindInvoiceRequest(LocalDate paymentDateMin, LocalDate paymentDateMax, String seller, Set<InvoiceStatus> invoiceStatuses) {
}
