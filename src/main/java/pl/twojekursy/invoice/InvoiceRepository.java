package pl.twojekursy.invoice;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

    List<Invoice> findByPaymentDateLessThanEqualOrderByPaymentDateDesc(LocalDate maxPaymentDate);

    @Query("select i from Invoice i where i.paymentDate <= :maxPaymentDate order by i.paymentDate desc")
    List<Invoice> findAndOrderByPaymentDateDesc(LocalDate maxPaymentDate);

    List<Invoice> findByPaymentDateLessThanEqual(LocalDate maxPaymentDate, Sort sort);

    @Query("select i from Invoice i where i.paymentDate <= :maxPaymentDate")
    List<Invoice> findByAndSort(LocalDate maxPaymentDate, Sort sort);

    List<Invoice> findAllByPaymentDateBetweenAndSellerStartingWithIgnoreCaseAndStatusIn(LocalDate paymentStartDate,
                                                                                        LocalDate paymentEndDate,
                                                                                        String seller,
                                                                                        Set<InvoiceStatus> statuses);

    @Query("select i from Invoice i where i.paymentDate between :paymentStartDate and :paymentEndDate " +
            "and lower(i.seller) like lower(:seller) " +
            "and i.status in :statuses ")
    List<Invoice> findBy(LocalDate paymentStartDate, LocalDate paymentEndDate, String seller, Set<InvoiceStatus> statuses);
}
