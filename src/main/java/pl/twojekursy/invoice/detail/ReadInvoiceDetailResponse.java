package pl.twojekursy.invoice.detail;

import lombok.Value;
import pl.twojekursy.invoice.ReadInvoiceResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
public class ReadInvoiceDetailResponse {
    Long id;

    Integer version;

    LocalDateTime createdDate;

    String productName;

    BigDecimal price;

    ReadInvoiceResponse invoice;

    public static ReadInvoiceDetailResponse from(InvoiceDetail invoiceDetail){
        return new ReadInvoiceDetailResponse(
                invoiceDetail.getId(),
                invoiceDetail.getVersion(),
                invoiceDetail.getCreatedDate(),
                invoiceDetail.getProductName(),
                invoiceDetail.getPrice(),
                ReadInvoiceResponse.from(invoiceDetail.getInvoice())
        );
    }
}
