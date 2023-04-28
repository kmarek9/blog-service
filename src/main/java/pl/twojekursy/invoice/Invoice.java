package pl.twojekursy.invoice;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.twojekursy.invoice.detail.InvoiceDetail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Invoice {

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

    // wymagane
    @NotNull
    private LocalDate paymentDate;

    // wymagane, notblank, max 150 znakow
    @NotNull
    @NotBlank
    @Size(max = 150)
    private String buyer;

    // wymagane, notblank,  max 150 znakow
    @NotNull
    @NotBlank
    @Size(max = 150)
    @NotAudited
    private String seller;

    //wymagane
    @NotNull
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @OneToMany(mappedBy = "invoice", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true )
    private Set<InvoiceDetail> invoiceDetails;

    public Invoice() {
    }

    public Invoice(Invoice old) {
        this.id = old.id;
        this.version = old.version;
        this.createdDate = old.createdDate;
        this.lastModifiedDate = old.lastModifiedDate;
        this.paymentDate = old.paymentDate;
        this.buyer = old.buyer;
        this.seller = old.seller;
        this.status = old.status;
        this.invoiceDetails = old.invoiceDetails;
    }

    public Invoice(LocalDate paymentDate, String buyer, String seller) {
        this.paymentDate = paymentDate;
        this.buyer = buyer;
        this.seller = seller;
        this.status = InvoiceStatus.ACTIVE;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public String getBuyer() {
        return buyer;
    }

    public String getSeller() {
        return seller;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<InvoiceDetail> getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(Set<InvoiceDetail> invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", paymentDate=" + paymentDate +
                ", buyer='" + buyer + '\'' +
                ", seller='" + seller + '\'' +
                ", status=" + status +
                '}';
    }
}
