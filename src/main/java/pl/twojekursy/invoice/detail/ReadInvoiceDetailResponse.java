package pl.twojekursy.invoice.detail;

import pl.twojekursy.invoice.ReadInvoiceResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReadInvoiceDetailResponse {
    private final Long id;

    private final Integer version;

    private final LocalDateTime createdDate;

    private final String productName;

    private final BigDecimal price;

    private final ReadInvoiceResponse invoice;


    public ReadInvoiceDetailResponse(Long id, Integer version, LocalDateTime createdDate, String productName, BigDecimal price, ReadInvoiceResponse invoice) {
        this.id = id;
        this.version = version;
        this.createdDate = createdDate;
        this.productName = productName;
        this.price = price;
        this.invoice = invoice;
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ReadInvoiceResponse getInvoice() {
        return invoice;
    }

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
