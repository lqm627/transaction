package com.lqm;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        List<StockEntity> purchaseList = new ArrayList<>();
        StockEntity saleStockEntity = new StockEntity();
        int num = 0;
        //购入总股数
        BigDecimal purchaseTotalCount = new BigDecimal("0");
        //购入总股数（可当天卖出的）
        BigDecimal canSaleTotalCount = new BigDecimal("0");
        //印花税率
        BigDecimal stampDutyRate = new BigDecimal("0.001");
        //买卖交易佣金费率
        BigDecimal transactionBasedRate = new BigDecimal("0.00025");
        //是否还继续输入购入记录，输入“完成”为结束，输入其他任意值为继续
        String ifFinish = "";
        //是沪市还是深市股票，输入“沪”为沪市，输入其他任意值为深市
        String stockAreaLogogram = "";
        do {
            StockEntity stockEntity = new StockEntity();
            System.out.println("是否是历史买入操作？-第" + (num + 1) + "条购入记录");
            String flag = s.next();
            System.out.println("是否是历史操作：" + (flag.equals("是") == true ? "是" : "否"));
            stockEntity.setFlag(flag);

            System.out.println("是沪市还是深市股票？-第" + (num + 1) + "条购入记录");
            stockAreaLogogram = s.next();
            System.out.println("是沪市还是深市股票：" + (stockAreaLogogram.equals("沪") == true ? "沪" : "深"));
            stockEntity.setStockAreaLogogram(stockAreaLogogram);

            System.out.println("输入购入价!-第" + (num + 1) + "条购入记录");
            BigDecimal inputPurchasePrice = s.nextBigDecimal();
            System.out.println("输入的购入价是：" + inputPurchasePrice);
            stockEntity.setInputPurchasePrice(inputPurchasePrice);

            System.out.println("输入购买的股数!-第" + (num + 1) + "条购入记录");
            BigDecimal inputPurchaseCount = s.nextBigDecimal();
            System.out.println("输入购买的股数是：" + inputPurchaseCount);
            stockEntity.setInputPurchaseCount(inputPurchaseCount);

            stockEntity.setTransactionCost(inputPurchasePrice.multiply(inputPurchaseCount));

            purchaseList.add(stockEntity);

            purchaseTotalCount = purchaseTotalCount.add(inputPurchaseCount);
            if (flag.equals("是")) {
                canSaleTotalCount = canSaleTotalCount.add(inputPurchaseCount);
            }
            System.out.println("第" + (num + 1) + "条购入记录手续费计算开始");
            //计算当前购入记录的交易手续费
            stockEntity.setTransactionFee(returnPurchaseTransactionCost
                    (true, inputPurchasePrice, inputPurchaseCount
                            , stockAreaLogogram, transactionBasedRate, null));
            System.out.println("第" + (num + 1) + "条购入记录手续费计算结束");
            stockEntity.setTransactionCostTotal(stockEntity.getTransactionCost().add(stockEntity.getTransactionFee()));

            System.out.println("当前已购买的总股数：" + purchaseTotalCount);
            System.out.println("---------------------------------------------");
            System.out.println("当前已购买的总股数（可当日卖出）：" + canSaleTotalCount);

            System.out.println("是否还要继续输入下一条购买记录？输入“完成”结束，输入任意值继续");
            ifFinish = s.next();
            System.out.println("输入的值是：" + ifFinish);

            num++;
            if (!ifFinish.equalsIgnoreCase("完成")) {
                continue;
            }
        } while (!ifFinish.equalsIgnoreCase("完成"));

        saleStockEntity.setStockAreaLogogram(stockAreaLogogram);

        System.out.println("输入卖出价!");
        BigDecimal inputSalePrice = s.nextBigDecimal();
        System.out.println("输入的卖出价是：" + inputSalePrice);

        BigDecimal inputSaleCount = BigDecimal.ZERO;
        do {
            System.out.println("输入卖出的股数!");
            inputSaleCount = s.nextBigDecimal();
            System.out.println("输入卖出的股数是：" + inputSaleCount);
            if (inputSaleCount.compareTo(canSaleTotalCount) == 1) {
                System.out.println("输入的卖出股数大于当前可卖出的股数（" + canSaleTotalCount + "），请重新输入!");
            }
        } while (inputSaleCount.compareTo(canSaleTotalCount) == 1);

        saleStockEntity.setInputSalePrice(inputSalePrice);
        saleStockEntity.setInputSaleCount(inputSaleCount);
        saleStockEntity.setTransactionCost(inputSalePrice.multiply(inputSaleCount));
        System.out.println("卖出记录手续费计算开始");
        saleStockEntity.setTransactionFee(returnPurchaseTransactionCost
                (false, inputSalePrice, inputSaleCount
                        , saleStockEntity.getStockAreaLogogram(), transactionBasedRate, stampDutyRate));
        System.out.println("卖出记录手续费计算结束");
        saleStockEntity.setTransactionCostTotal(saleStockEntity.getTransactionCost().subtract(saleStockEntity.getTransactionFee()));

        System.out.println(new Gson().toJson(saleStockEntity));
        System.out.println(new Gson().toJson(purchaseList));

        System.out.println("从一开始买入该股开始计算成本的前提下，这次卖出操作的最终盈利是：" + returnUltimatelyBenefit(purchaseList, saleStockEntity, transactionBasedRate, stampDutyRate));
    }

    //返回这次卖出操作的最终收益
    private static BigDecimal returnUltimatelyBenefit(List<StockEntity> purchaseList, StockEntity saleStockEntity, BigDecimal transactionBasedRate, BigDecimal stampDutyRate) {
        List<StockEntity> purchaseListNEW = new ArrayList<>(purchaseList);
        BigDecimal residualCostRecordTotal = new BigDecimal("0");
        BigDecimal residualCountNewTotal = new BigDecimal("0");
        BigDecimal inputSaleCountNEW = new BigDecimal(saleStockEntity.getInputSaleCount().toString());
        BigDecimal saleCostTotal = new BigDecimal("0");

        StockEntity stockEntity = new StockEntity();
        do {
            stockEntity = purchaseList.stream()
                    .filter(o -> o.getFlag().equals("是"))
                    .min(((o1, o2) -> o1.getInputPurchasePrice().compareTo(o2.getInputPurchasePrice()))).get();

            saleStockEntity.setInputSaleCount(saleStockEntity.getInputSaleCount().subtract(stockEntity.getInputPurchaseCount()));

            if (saleStockEntity.getInputSaleCount().compareTo(BigDecimal.ZERO) == 1) {
                System.out.println("卖出记录扣减可卖出股票手续费计算开始--需要继续扣减");
                saleCostTotal = saleCostTotal
                        .add(stockEntity.getInputPurchasePrice().multiply(stockEntity.getInputPurchaseCount()))
                        .add(returnPurchaseTransactionCost
                                (true, stockEntity.getInputPurchasePrice(), stockEntity.getInputPurchaseCount()
                                        , stockEntity.getStockAreaLogogram(), transactionBasedRate, null));
//                System.out.println("%%%%%%%%%%%%%%%%%%%" + saleCostTotal);
//                System.out.println("^^^^^^^^^^^^^^^^^^^" + stockEntity.getTransactionCostTotal());
                System.out.println("卖出记录扣减可卖出股票手续费计算结束");
                stockEntity.setInputPurchaseCount(BigDecimal.ZERO);
                purchaseList.remove(stockEntity);
                purchaseListNEW.remove(stockEntity);
                purchaseListNEW.add(stockEntity);
            } else {
                System.out.println("卖出记录扣减可卖出股票手续费计算开始--完结");
                saleCostTotal = saleCostTotal
                        .add(stockEntity.getInputPurchasePrice().multiply(stockEntity.getInputPurchaseCount().add(saleStockEntity.getInputSaleCount())))
                        .add(returnPurchaseTransactionCost
                                (true, stockEntity.getInputPurchasePrice(), stockEntity.getInputPurchaseCount().add(saleStockEntity.getInputSaleCount())
                                        , stockEntity.getStockAreaLogogram(), transactionBasedRate, null));
                System.out.println("卖出记录扣减可卖出股票手续费计算结束");
                stockEntity.setInputPurchaseCount(saleStockEntity.getInputSaleCount().abs());
                purchaseListNEW.remove(stockEntity);
                purchaseListNEW.add(stockEntity);
            }
        } while (saleStockEntity.getInputSaleCount().compareTo(BigDecimal.ZERO) == 1);

        System.out.println("卖出股票的总收益计算开始（扣除手续费）");
        System.out.println("卖出股票的总收益（扣除手续费）：" + saleStockEntity.getInputSalePrice()
                .multiply(inputSaleCountNEW)
                .subtract(returnPurchaseTransactionCost
                        (false, saleStockEntity.getInputSalePrice(), inputSaleCountNEW, saleStockEntity.getStockAreaLogogram(), transactionBasedRate, stampDutyRate)));
        System.out.println("卖出股票的总收益计算结束（扣除手续费）");
        System.out.println("卖出股票的原先购入成本：" + saleCostTotal);

        for (int i = 0; i < purchaseListNEW.size(); i++) {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            BigDecimal residualCostRecord = purchaseListNEW.get(i).getInputPurchasePrice()
                    .multiply(purchaseListNEW.get(i).getInputPurchaseCount())
                    .add(returnPurchaseTransactionCost(true, purchaseListNEW.get(i).getInputPurchasePrice(), purchaseListNEW.get(i).getInputPurchaseCount()
                            , purchaseListNEW.get(i).getStockAreaLogogram(), transactionBasedRate, null));
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("股票" + purchaseListNEW.get(i).getInputPurchasePrice() + "元购入价" +
                    "，剩余" + purchaseListNEW.get(i).getInputPurchaseCount() + "股" +
                    "，购入成本剩余：" + residualCostRecord + (purchaseListNEW.get(i).getFlag().equals("是") ? "（历史购入）" : "（今日购入）"));

            residualCostRecordTotal = residualCostRecordTotal.add(residualCostRecord);
            residualCountNewTotal = residualCountNewTotal.add(purchaseListNEW.get(i).getInputPurchaseCount());
        }
        System.out.println("剩余股票的总购入成本:" + residualCostRecordTotal);

        System.out.println("剩余股票的最新价值：" + saleStockEntity.getInputSalePrice().multiply(residualCountNewTotal));

        return saleStockEntity.getInputSalePrice().multiply(inputSaleCountNEW).subtract(saleCostTotal);
    }

    //返回当前交易记录的手续费
    private static BigDecimal returnPurchaseTransactionCost(boolean ifBuy, BigDecimal inputPurchasePrice, BigDecimal inputPurchaseCount
            , String stockAreaLogogram, BigDecimal transactionBasedRate, BigDecimal stampDutyRate) {
        //当前交易记录的手续费总额
        BigDecimal purchaseTransactionCost = new BigDecimal("0");
        //只有沪市才需要过户费
        if (stockAreaLogogram.equals("沪")) {
            purchaseTransactionCost = purchaseTransactionCost.add(returnTransferFee(inputPurchasePrice, inputPurchaseCount));
            System.out.println("过户费为：" + purchaseTransactionCost);
        }

        BigDecimal transactionBasedCost = inputPurchasePrice.multiply(inputPurchaseCount).multiply(transactionBasedRate);
        if (inputPurchaseCount.compareTo(BigDecimal.ZERO) == 0) {
            purchaseTransactionCost = BigDecimal.ZERO;
            System.out.println("交易佣金费为：" + 0);
        } else if (transactionBasedCost.compareTo(new BigDecimal("5")) == -1) {
            purchaseTransactionCost = purchaseTransactionCost.add(new BigDecimal("5"));
            System.out.println("交易佣金费为：" + 5);
        } else {
            purchaseTransactionCost = purchaseTransactionCost.add(transactionBasedCost);
            System.out.println("交易佣金费为：" + transactionBasedCost);
        }

        //是否是买入操作，卖出才需要收取印花税
        if (!ifBuy && stampDutyRate != null) {
            BigDecimal stampDutyRateCost = inputPurchasePrice.multiply(inputPurchaseCount).multiply(stampDutyRate);
            purchaseTransactionCost = purchaseTransactionCost.add(stampDutyRateCost);
            System.out.println("印花税费为：" + stampDutyRateCost);
        }

        return purchaseTransactionCost;
    }

    //返回当前交易记录的过户费
    private static BigDecimal returnTransferFee(BigDecimal inputTransferPrice, BigDecimal inputTransferCount) {
        //当前交易记录的股数是过户费标准的多少倍（每1000股1元，不足1000股同样收取1元，最低1元起）
        BigDecimal transferFee = null;
        if (inputTransferCount.compareTo(BigDecimal.ZERO) == 0) {
            transferFee = BigDecimal.ZERO;
            return transferFee;
        } else if (inputTransferCount.compareTo(new BigDecimal("1000")) == -1) {
            transferFee = new BigDecimal("1");
            System.out.println("当前股票" + inputTransferPrice + "元交易记录，过户费为：" + transferFee);
            return transferFee;
        } else {
            transferFee = inputTransferCount.multiply(new BigDecimal("0.001"));
            return transferFee;
        }
    }
}
