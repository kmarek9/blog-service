package pl.twojekursy.test.helper;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.invoice.Invoice;
import pl.twojekursy.invoice.detail.InvoiceDetail;

import java.math.BigDecimal;

@Component
public class InvoiceDetailCreator {
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public InvoiceDetail createInvoiceDetail(Invoice invoice) {
        InvoiceDetail invoiceDetail = InvoiceDetail.builder()
                .productName("productName")
                .price(BigDecimal.TEN)
                .invoice(invoice)
                .build();
        entityManager.persist(invoiceDetail);
        return invoiceDetail;
    }
}
