package pl.twojekursy.invoice.detail;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.twojekursy.invoice.Invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@Data
@ToString(exclude = {"invoice"})
@EqualsAndHashCode(exclude = {"invoice"})
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InvoiceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @NotNull
    private Integer version;

    // wymagane
    @CreatedDate
    @NotNull
    private LocalDateTime createdDate;

    @LastModifiedDate
    @NotNull
    private LocalDateTime lastModifiedDate;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String productName;

    @NotNull
    private BigDecimal price;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Invoice invoice;

    public InvoiceDetail(String productName, BigDecimal price, Invoice invoice) {
        this.productName = productName;
        this.price = price;
        this.invoice = invoice;
    }
}
