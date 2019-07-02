package com.example.insertandretrievefromfirebasenew;

public class Cart {
    private String invoiceNumber,customerName,ShopName,prodID,prodName,status,bookingCode,imageURI;
    private int prodPrice,quantity;

    Cart(){}
    public Cart(String invoiceNumber, String customerName, String shopName, String prodID, String prodName, String status, String bookingCode, String imageURI, int prodPrice, int quantity) {
        this.invoiceNumber = invoiceNumber;
        this.customerName = customerName;
        this.ShopName = shopName;
        this.prodID = prodID;
        this.prodName = prodName;
        this.status = status;
        this.bookingCode = bookingCode;
        this.imageURI = imageURI;
        this.prodPrice = prodPrice;
        this.quantity = quantity;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(int prodPrice) {
        this.prodPrice = prodPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}
