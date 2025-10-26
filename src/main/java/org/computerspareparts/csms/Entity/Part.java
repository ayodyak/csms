package org.computerspareparts.csms.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "part")
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer partId;

    private String name;
    private String brand;
    private String category;
    private Double price;
    private Integer stockQuantity;
    private Integer recorderLevel;

    // Getters and setters
    public Integer getPartId() { return partId; }
    public void setPartId(Integer partId) { this.partId = partId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public Integer getRecorderLevel() { return recorderLevel; }
    public void setRecorderLevel(Integer recorderLevel) { this.recorderLevel = recorderLevel; }
}

