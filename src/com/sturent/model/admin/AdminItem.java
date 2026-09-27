package com.sturent.model.admin;

import java.math.BigDecimal;

public class AdminItem {

    private int itemId;
    private String userId;
    private String ownerName;
    private String title;
    private String listingType;
    private BigDecimal salePrice;
    private BigDecimal rentPrice;
    private String rentalUnit;
    private BigDecimal deposit;
    private String status;

    public AdminItem(
            int itemId,
            String userId,
            String ownerName,
            String title,
            String listingType,
            BigDecimal salePrice,
            BigDecimal rentPrice,
            String rentalUnit,
            BigDecimal deposit,
            String status
    ) {
        this.itemId = itemId;
        this.userId = userId;
        this.ownerName = ownerName;
        this.title = title;
        this.listingType = listingType;
        this.salePrice = salePrice;
        this.rentPrice = rentPrice;
        this.rentalUnit = rentalUnit;
        this.deposit = deposit;
        this.status = status;
    }

    public int getItemId() {
        return itemId;
    }

    public String getUserId() {
        return userId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getTitle() {
        return title;
    }

    public String getListingType() {
        return listingType;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public BigDecimal getRentPrice() {
        return rentPrice;
    }

    public String getRentalUnit() {
        return rentalUnit;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public String getStatus() {
        return status;
    }
}