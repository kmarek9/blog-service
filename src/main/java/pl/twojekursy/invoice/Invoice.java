package pl.twojekursy.invoice;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Invoice {
    //zad dom
    //stworzyc InvoiceRepository
    //stworzyć InvoiceController
    //stworzyć w kontrolerze metody create (POST) i read (GET)
    //w creatcie stworzyć  20 encji Invoice i zapisac do bazy
    // w read, odczytać po ID,  usunać po ID i encji, wyszukać wszytkie, zupdatować

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    private LocalDate paymentDate;

    private String buyer;

    private String seller;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    public Invoice() {
    }

    public Invoice(Long id, LocalDateTime createdDate, LocalDate paymentDate, String buyer, String seller, InvoiceStatus status) {
        this.id = id;
        this.createdDate = createdDate;
        this.paymentDate = paymentDate;
        this.buyer = buyer;
        this.seller = seller;
        this.status = status;
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
