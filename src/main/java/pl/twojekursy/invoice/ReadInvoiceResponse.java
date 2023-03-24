package pl.twojekursy.invoice;

import pl.twojekursy.post.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReadInvoiceResponse(Long id, Integer version, LocalDateTime createdDate, LocalDate paymentDate, String buyer,
                                  String seller, InvoiceStatus status) {

    public static ReadInvoiceResponse from(Invoice invoice){
        return new ReadInvoiceResponse(
                invoice.getId(),
                invoice.getVersion(),
                invoice.getCreatedDate(),
                invoice.getPaymentDate(),
                invoice.getBuyer(),
                invoice.getSeller(),
                invoice.getStatus()
        );
    }
}
