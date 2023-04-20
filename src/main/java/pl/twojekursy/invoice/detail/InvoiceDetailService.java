package pl.twojekursy.invoice.detail;

import org.springframework.stereotype.Service;
import pl.twojekursy.invoice.Invoice;
import pl.twojekursy.invoice.InvoiceService;

@Service
public class InvoiceDetailService {
    private final InvoiceDetailRepository invoiceDetailRepository;

    private final InvoiceService invoiceService;

    public InvoiceDetailService(InvoiceDetailRepository invoiceDetailRepository, InvoiceService invoiceService) {
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.invoiceService = invoiceService;
    }

    public void create(CreateInvoiceDetailRequest invoiceDetailRequest){
        Invoice invoice = invoiceService.findInvoiceById(invoiceDetailRequest.getInvoiceId());

        InvoiceDetail invoiceDetail = new InvoiceDetail(
                invoiceDetailRequest.getProductName(),
                invoiceDetailRequest.getPrice(),
                invoice
        );

        invoiceDetailRepository.save(invoiceDetail);
    }
}
