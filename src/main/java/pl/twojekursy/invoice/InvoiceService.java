package pl.twojekursy.invoice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.twojekursy.util.LogUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public void create(CreateInvoiceRequest invoiceRequest){
        Invoice invoice = new Invoice(
              invoiceRequest.paymentDate(),
              invoiceRequest.buyer(),
              invoiceRequest.seller()
        ) ;

        invoiceRepository.save(invoice);
    }

    public ReadInvoiceResponse findById(Long id) {
        return invoiceRepository.findById(id)
                .map(ReadInvoiceResponse::from)
                .orElseThrow(EntityNotFoundException::new);
    }

    public void update(Long id, UpdateInvoiceRequest updateInvoiceRequest) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Invoice newInvoice = new Invoice(invoice);

        newInvoice.setPaymentDate(updateInvoiceRequest.paymentDate());
        newInvoice.setSeller(updateInvoiceRequest.seller());
        newInvoice.setBuyer(updateInvoiceRequest.buyer());
        newInvoice.setVersion(updateInvoiceRequest.version());

        invoiceRepository.save(newInvoice);
    }

    public void delete(Long id) {
        invoiceRepository.deleteById(id);
    }

    public void archive(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Invoice newInvoice = new Invoice(invoice);

        newInvoice.setStatus(InvoiceStatus.DELETED);
        invoiceRepository.save(newInvoice);
    }

    //"select i from Invoice i where i.paymentDate between :paymentStartDate and :paymentEndDate " +
    //        "and lower(i.seller) like lower(:seller) " +
    //        "and i.status in :statuses "
    public Page<FindInvoiceResponse> find(FindInvoiceRequest invoiceRequest, Pageable pageable) {
        Specification<Invoice> invoiceSpecification = prepareSpecs(invoiceRequest);

        return invoiceRepository.findAll(invoiceSpecification, pageable)
                .map(FindInvoiceResponse::from);
    }

    public Page<FindInvoiceResponse> find(String sellerContaining,
                                          String buyerContaining,
                                          int page,
                                          int size){
        return invoiceRepository.findByBuyerContainingAndSellerContainingAndStatusInOrderByPaymentDate(
                buyerContaining, sellerContaining,
                Set.of(InvoiceStatus.ACTIVE, InvoiceStatus.DRAFT),
                PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")))
        )
                .map(FindInvoiceResponse::from);
    }
    public void find2() {

        log(() -> invoiceRepository.findByPaymentDateLessThanEqualOrderByPaymentDateDesc(
                LocalDate.of(2023,3,28)
            ) ,
                "findByPaymentDateLessThenEqualOrderByPaymentDateDesc"
        );

        log(() -> invoiceRepository.findAndOrderByPaymentDateDesc(
                        LocalDate.of(2023,3,28)
                ) ,
                "findAndOrderByPaymentDateDesc"
        );

        log(() -> invoiceRepository.findByPaymentDateLessThanEqual(
                        LocalDate.of(2023,3,28),
                        Sort.by(Sort.Order.desc("paymentDate"),
                                Sort.Order.asc("id")
                                )
                ) ,
                "findByPaymentDateLessThenEqualOrderByPaymentDateDesc"
        );

        log(() -> invoiceRepository.findByAndSort(
                        LocalDate.of(2023,3,28),
                        Sort.by(Sort.Order.desc("paymentDate"),
                                Sort.Order.asc("id")
                        )
                ) ,
                "findByAndSort"
        );


        log(()-> invoiceRepository.findAllByPaymentDateBetweenAndSellerStartingWithIgnoreCaseAndStatusIn(
                LocalDate.of(2023,3,28),
                LocalDate.of(2023,3,29),
                "Sel",
                Set.of(InvoiceStatus.ACTIVE, InvoiceStatus.DRAFT)
                ),
                "findAllByPaymentDateBetweenAndSellerStartingWithIgnoreCaseAndStatusIn"
        );


        log(()-> invoiceRepository.findBy(
                        LocalDate.of(2023,3,28),
                        LocalDate.of(2023,3,29),
                        "Sel%",
                        Set.of(InvoiceStatus.ACTIVE, InvoiceStatus.DRAFT)
                ),
                "findBy"
        );

        //zad dom
        //skopiowac poniższą metodą i dodać stronicowanie i zwracać Page
        //w serwisie wywołac 4x
        // - dla daty 2023-03-28 ,
        // - okno o wielkosci 3
        // - posortować po seller malejaco
        // - numer strony 0, 1,2, 3 i obejrzec wyniki i zapytania
        LogUtil.logPage(() -> invoiceRepository.findByAndSort(
                LocalDate.of(2023,3,28),
                PageRequest.of(0,3,Sort.by(Sort.Order.desc("seller")))
        ), "findByAndSort");

        LogUtil.logPage(() -> invoiceRepository.findByAndSort(
                LocalDate.of(2023,3,28),
                PageRequest.of(1,3,Sort.by(Sort.Order.desc("seller")))
        ), "findByAndSort");

        LogUtil.logPage(() -> invoiceRepository.findByAndSort(
                LocalDate.of(2023,3,28),
                PageRequest.of(2,3,Sort.by(Sort.Order.desc("seller")))
        ), "findByAndSort");

        LogUtil.logPage(() -> invoiceRepository.findByAndSort(
                LocalDate.of(2023,3,28),
                PageRequest.of(3,3,Sort.by(Sort.Order.desc("seller")))
        ), "findByAndSort");

    }

    private void log(Supplier<List<Invoice>> listSupplier, String methodName){
        System.out.println("---------------" + methodName + " ---------------------");

        listSupplier.get().forEach(System.out::println);
    }

    private static Specification<Invoice> prepareSpecs(FindInvoiceRequest invoiceRequest) {
        Specification<Invoice> specification = Specification.where(null);

        if(invoiceRequest.paymentDateMin()!=null && invoiceRequest.paymentDateMax()!=null) {
            specification = specification.and((root, query, cr) -> cr.between(root.get("paymentDate"),
                    invoiceRequest.paymentDateMin(),
                    invoiceRequest.paymentDateMax())
            );
        }

        if(invoiceRequest.seller()!=null) {
            specification = specification.and(
                    (root, query, cr) -> likeIgnoreCase(cr, root.get("seller"), invoiceRequest.seller())
            );
        }

        if(invoiceRequest.invoiceStatuses()!=null) {
            specification = specification.and((root, query, cr) ->
                    root.get("status").in(invoiceRequest.invoiceStatuses())
            );
        }
        return specification;
    }

    private static Specification<Invoice> prepareSpecsUsingPredicates(FindInvoiceRequest invoiceRequest) {
        return (root, query, cr) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(invoiceRequest.paymentDateMin()!=null && invoiceRequest.paymentDateMax()!=null) {

                predicates.add(cr.between(root.get("paymentDate"),
                        invoiceRequest.paymentDateMin(),
                        invoiceRequest.paymentDateMax())
                );
            }

            if(invoiceRequest.seller()!=null) {
                predicates.add(likeIgnoreCase(cr, root.get("seller"), invoiceRequest.seller()));
            }

            if(invoiceRequest.invoiceStatuses()!=null) {
                predicates.add(root.get("status").in(invoiceRequest.invoiceStatuses()));
            }

            return cr.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static Predicate likeIgnoreCase(CriteriaBuilder cr, Path<String> fieldPath, String text) {
        return cr.like(cr.lower(fieldPath),
                "%" + text.toLowerCase() + "%");
    }
}
