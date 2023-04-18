package pl.twojekursy.invoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface InvoiceRepository extends CrudRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {

    List<Invoice> findByPaymentDateLessThanEqualOrderByPaymentDateDesc(LocalDate maxPaymentDate);

    Page<Invoice> findByBuyerContainingAndSellerContainingAndStatusInOrderByPaymentDate(
                                                    String buyer,
                                                    String Seller,
                                                    Set<InvoiceStatus> invoiceStatuses,
                                                    Pageable pageable
    );

    @Query("select i from Invoice i where i.paymentDate <= :maxPaymentDate order by i.paymentDate desc")
    List<Invoice> findAndOrderByPaymentDateDesc(LocalDate maxPaymentDate);

    List<Invoice> findByPaymentDateLessThanEqual(LocalDate maxPaymentDate, Sort sort);

    //zad dom
    //skopiowac poniższą metodą i dodać stronicowanie i zwracać Page
    //w serwisie wywołac 4x
    // - dla daty 2023-03-28 ,
    // - okno o wielkosci 3
    // - posortować po seller malejaco
    // - numer strony 0, 1,2, 3 i obejrzec wyniki i zapytania

    @Query("select i from Invoice i where i.paymentDate <= :maxPaymentDate")
    List<Invoice> findByAndSort(LocalDate maxPaymentDate, Sort sort);

    @Query("select i from Invoice i where i.paymentDate <= :maxPaymentDate")
    Page<Invoice> findByAndSort(LocalDate maxPaymentDate, Pageable pageable);

    List<Invoice> findAllByPaymentDateBetweenAndSellerStartingWithIgnoreCaseAndStatusIn(LocalDate paymentStartDate,
                                                                                        LocalDate paymentEndDate,
                                                                                        String seller,
                                                                                        Set<InvoiceStatus> statuses);

    @Query("select i from Invoice i where i.paymentDate between :paymentStartDate and :paymentEndDate " +
            "and lower(i.seller) like lower(:seller) " +
            "and i.status in :statuses ")
    List<Invoice> findBy(LocalDate paymentStartDate, LocalDate paymentEndDate, String seller, Set<InvoiceStatus> statuses);
}
