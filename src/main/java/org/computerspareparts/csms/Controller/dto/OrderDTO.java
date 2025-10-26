package org.computerspareparts.csms.Controller.dto;

import java.util.Date;
import java.util.List;

public class OrderDTO {
    public Integer orderId;
    public String customerName;
    public String employeeName;
    public Date orderDate;
    public String status;
    public Double totalAmount;
    public List<OrderItemDTO> items;

    public static class OrderItemDTO {
        public Integer partId;
        public String partName;
        public Integer quantity;
        public Double price;
    }
}
