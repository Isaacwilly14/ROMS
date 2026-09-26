package com.nkumba.roms.models;

public class Order {
    private int id;
    private String customerName;
    private String customerLocation;
    private String customerContact;
    private String customerEmail;
    private double totalAmount;
    private String orderDate;
    private String status;
    private int productId;
    private int quantity;

    public Order(int id, String customerName, String customerLocation, String customerContact, String customerEmail, double totalAmount, String orderDate, String status, int productId, int quantity) {
        this.id = id;
        this.customerName = customerName;
        this.customerLocation = customerLocation;
        this.customerContact = customerContact;
        this.customerEmail = customerEmail;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getCustomerLocation() { return customerLocation; }
    public String getCustomerContact() { return customerContact; }
    public String getCustomerEmail() { return customerEmail; }
    public double getTotalAmount() { return totalAmount; }
    public String getOrderDate() { return orderDate; }
    public String getStatus() { return status; }
    public int getProductId() { return productId; }
    public int getQuantity() { return quantity; }
}