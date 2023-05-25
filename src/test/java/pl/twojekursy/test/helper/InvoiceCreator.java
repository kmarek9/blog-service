package pl.twojekursy.test.helper;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.invoice.Invoice;
import pl.twojekursy.invoice.InvoiceStatus;
import pl.twojekursy.post.Post;
import pl.twojekursy.post.PostScope;
import pl.twojekursy.post.PostStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class InvoiceCreator {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private InvoiceDetailCreator invoiceDetailCreator;


    @Transactional
    public Invoice createInvoice() {
        LocalDate paymentDate = LocalDate.now().plusDays(1);
        Invoice invoice = Invoice.builder()
                .paymentDate(paymentDate)
                .buyer("buyer")
                .seller("seller")
                .status(InvoiceStatus.ACTIVE)
                .build();
        entityManager.persist(invoice);
        return invoice;
    }

    @Transactional
    public Invoice createInvoiceWithOneInvoiceDetail() {
        Invoice invoice = createInvoice();
        invoiceDetailCreator.createInvoiceDetail(invoice);

        return invoice;
    }
}
