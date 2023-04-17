package pl.twojekursy.invoice;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FindInvoiceResponse(Long id, LocalDate paymentDate, String buyer,
                                  String seller) {

    public static FindInvoiceResponse from(Invoice invoice){
        return new FindInvoiceResponse(
                invoice.getId(),
                invoice.getPaymentDate(),
                invoice.getBuyer(),
                invoice.getSeller()
        );
    }
}
