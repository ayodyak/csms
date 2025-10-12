package Entity;

public class PurchaseRequestDTO {
    private Long requestId;
    private Long productId;
    private Long supplierId;
    private int quantity;
    private String status;
    private String requestDate;

    public PurchaseRequestDTO() {}

    public PurchaseRequestDTO(Long requestId, Long productId, Long supplierId, int quantity, String status, String requestDate) {
        this.requestId = requestId;
        this.productId = productId;
        this.supplierId = supplierId;
        this.quantity = quantity;
        this.status = status;
        this.requestDate = requestDate;
    }

    // Getters and Setters
    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRequestDate() { return requestDate; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }
}
