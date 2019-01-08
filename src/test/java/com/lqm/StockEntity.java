package com.lqm;

import java.math.BigDecimal;

public class StockEntity {
    private BigDecimal inputPurchasePrice;
    private BigDecimal inputPurchaseCount;
    private BigDecimal inputSalePrice;
    private BigDecimal inputSaleCount;
    private BigDecimal purchaseCost;
    private String flag;

    public BigDecimal getInputPurchasePrice() {
        return inputPurchasePrice;
    }

    public void setInputPurchasePrice(BigDecimal inputPurchasePrice) {
        this.inputPurchasePrice = inputPurchasePrice;
    }

    public BigDecimal getInputPurchaseCount() {
        return inputPurchaseCount;
    }

    public void setInputPurchaseCount(BigDecimal inputPurchaseCount) {
        this.inputPurchaseCount = inputPurchaseCount;
    }

    public BigDecimal getInputSalePrice() {
        return inputSalePrice;
    }

    public void setInputSalePrice(BigDecimal inputSalePrice) {
        this.inputSalePrice = inputSalePrice;
    }

    public BigDecimal getInputSaleCount() {
        return inputSaleCount;
    }

    public void setInputSaleCount(BigDecimal inputSaleCount) {
        this.inputSaleCount = inputSaleCount;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public BigDecimal getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(BigDecimal purchaseCost) {
        this.purchaseCost = purchaseCost;
    }
}
