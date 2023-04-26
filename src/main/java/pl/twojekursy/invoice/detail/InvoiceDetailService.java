package pl.twojekursy.invoice.detail;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.invoice.Invoice;
import pl.twojekursy.invoice.InvoiceService;

import java.util.Optional;

@Service
public class InvoiceDetailService {
    private final InvoiceDetailRepository invoiceDetailRepository;

    private final InvoiceService invoiceService;

    public InvoiceDetailService(InvoiceDetailRepository invoiceDetailRepository, InvoiceService invoiceService) {
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.invoiceService = invoiceService;
    }

    @Transactional
    public void create(CreateInvoiceDetailRequest invoiceDetailRequest){
        Invoice invoice = invoiceService.findInvoiceById(invoiceDetailRequest.getInvoiceId());

        InvoiceDetail invoiceDetail = new InvoiceDetail(
                invoiceDetailRequest.getProductName(),
                invoiceDetailRequest.getPrice(),
                invoice
        );

        invoiceDetailRepository.save(invoiceDetail);
    }

    public ReadInvoiceDetailResponse findById(Long id) {
        Optional<InvoiceDetail> maybeInvoiceDetail = invoiceDetailRepository.findByIdFetchInvoice(id);
        Optional<ReadInvoiceDetailResponse> readInvoiceDetailResponse = maybeInvoiceDetail.map(ReadInvoiceDetailResponse::from);
        return readInvoiceDetailResponse.orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void update(Long id, UpdateInvoiceDetailRequest updateInvoiceDetailRequest) {
        InvoiceDetail invoiceDetail = invoiceDetailRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        InvoiceDetail newInvoiceDetail = new InvoiceDetail(invoiceDetail.getId(),
                updateInvoiceDetailRequest.getVersion(),
                invoiceDetail.getCreatedDate(),
                invoiceDetail.getLastModifiedDate(),
                updateInvoiceDetailRequest.getProductName(),
                updateInvoiceDetailRequest.getPrice(),
                invoiceDetail.getInvoice());

        invoiceDetailRepository.save(newInvoiceDetail);
    }
}
