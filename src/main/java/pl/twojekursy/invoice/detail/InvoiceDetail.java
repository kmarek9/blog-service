package pl.twojekursy.invoice.detail;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    public InvoiceDetail() {
    }

    public InvoiceDetail(String productName, BigDecimal price, Invoice invoice) {
        this.productName = productName;
        this.price = price;
        this.invoice = invoice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public String toString() {
        return "InvoiceDetail{" +
                "id=" + id +
                ", version=" + version +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                '}';
    }
}
