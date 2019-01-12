package com.lqm;

import java.math.BigDecimal;

public class StockEntity {
    /**
     * 购入价格
     */
    private BigDecimal inputPurchasePrice;
    /**
     * 购入股数
     */
    private BigDecimal inputPurchaseCount;
    /**
     * 卖出价格
     */
    private BigDecimal inputSalePrice;
    /**
     * 卖出股数
     */
    private BigDecimal inputSaleCount;
    /**
     * 交易金额
     */
    private BigDecimal transactionCost;
    /**
     * 是否是历史历史购入记录，“是”或其他任意值
     */
    private String flag;
    /**
     * 是沪市还是深市股票，“沪”或其他任意值
     */
    private String stockAreaLogogram;
    /**
     * 交易手续费总额
     */
    private BigDecimal transactionFee;
    /**
     * 交易总金额（交易金额+交易手续费总额）
     */
    private BigDecimal transactionCostTotal;

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

    public String getStockAreaLogogram() {
        return stockAreaLogogram;
    }

    public void setStockAreaLogogram(String stockAreaLogogram) {
        this.stockAreaLogogram = stockAreaLogogram;
    }

    public BigDecimal getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(BigDecimal transactionFee) {
        this.transactionFee = transactionFee;
    }

    public BigDecimal getTransactionCost() {
        return transactionCost;
    }

    public void setTransactionCost(BigDecimal transactionCost) {
        this.transactionCost = transactionCost;
    }

    public BigDecimal getTransactionCostTotal() {
        return transactionCostTotal;
    }

    public void setTransactionCostTotal(BigDecimal transactionCostTotal) {
        this.transactionCostTotal = transactionCostTotal;
    }
}
