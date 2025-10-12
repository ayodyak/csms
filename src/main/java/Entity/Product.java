package Entity;


import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long products_id;

    private String name;

    private String sku;

    // Product → Purchase Requests
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PurchaseRequest> purchaseRequests;

    public Long getProducts_id() {
        return products_id;
    }

    public void setProducts_id(Long products_id) {
        this.products_id = products_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<PurchaseRequest> getPurchaseRequests() {
        return purchaseRequests;
    }

    public void setPurchaseRequests(List<PurchaseRequest> purchaseRequests) {
        this.purchaseRequests = purchaseRequests;
    }
}


