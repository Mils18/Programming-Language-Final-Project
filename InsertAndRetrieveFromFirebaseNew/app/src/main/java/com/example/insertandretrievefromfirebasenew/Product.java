package com.example.insertandretrievefromfirebasenew;

public class Product {
    private String prodName;
    private String description;
    private int price;
    private int stock;
    private String shopName;
    private String imageName;
    private String imageURI;
    private String productID;


    public Product(){
    }


    public Product(String prodName, String description, int price, int stock, String shopName, String imageName, String imageURI, String productID) {
        this.prodName = prodName;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.shopName = shopName;
        this.imageName = imageName;
        this.imageURI = imageURI;
        this.productID = productID;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getproductID() {
        return productID;
    }

    public void setproductID(String productID) {
        this.productID = productID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }


    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}
