package pl.twojekursy.invoice;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    private LocalDate paymentDate;

    private String buyer;

    private String seller;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;
}
