package Entity;


import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;

    private String name;

    @Column(unique = true)
    private String email;

    private String contactNo;

    private String address;

    private String passwordHash;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Supplier → Purchase Requests
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PurchaseRequest> purchaseRequests;

    // Supplier → Deliveries
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Delivery> deliveries;

    // Supplier → Payments
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SupplierPayment> payments;

    // Getters and Setters
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<PurchaseRequest> getPurchaseRequests() { return purchaseRequests; }
    public void setPurchaseRequests(List<PurchaseRequest> purchaseRequests) { this.purchaseRequests = purchaseRequests; }

    public List<Delivery> getDeliveries() { return deliveries; }
    public void setDeliveries(List<Delivery> deliveries) { this.deliveries = deliveries; }

    public List<SupplierPayment> getPayments() { return payments; }
    public void setPayments(List<SupplierPayment> payments) { this.payments = payments; }
}
