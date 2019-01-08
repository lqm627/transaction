package com.lqm;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        List<StockEntity> purchaseList = new ArrayList<>();
        int num = 0;
        BigDecimal purchaseTotalCount = new BigDecimal("0");
        BigDecimal canSaleTotalCount = new BigDecimal("0");
        String ifFinish = "";
        do {
            StockEntity stockEntity = new StockEntity();
            System.out.println("是否是历史买入操作？-第" + (num + 1) + "条购入记录");
            String flag = s.next();
            System.out.println("是否是历史操作：" + (flag.equals("是") == true ? "是" : "否"));
            stockEntity.setFlag(flag);

            System.out.println("输入购入价!-第" + (num + 1) + "条购入记录");
            BigDecimal inputPurchasePrice = s.nextBigDecimal();
            System.out.println("输入的购入价是：" + inputPurchasePrice);
            stockEntity.setInputPurchasePrice(inputPurchasePrice);

            System.out.println("输入购买的股数!-第" + (num + 1) + "条购入记录");
            BigDecimal inputPurchaseCount = s.nextBigDecimal();
            System.out.println("输入购买的股数是：" + inputPurchaseCount);
            stockEntity.setInputPurchaseCount(inputPurchaseCount);

            stockEntity.setPurchaseCost(inputPurchasePrice.multiply(inputPurchaseCount));

            purchaseList.add(stockEntity);

            purchaseTotalCount = purchaseTotalCount.add(inputPurchaseCount);
            if (flag.equals("是")) {
                canSaleTotalCount = canSaleTotalCount.add(inputPurchaseCount);
            }
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

        System.out.println("输入卖出价!");
        BigDecimal inputSalePrice = s.nextBigDecimal();
        System.out.println("输入的卖出价是：" + inputSalePrice);

        BigDecimal inputSaleCount = new BigDecimal("0");
        do {
            System.out.println("输入卖出的股数!");
            inputSaleCount = s.nextBigDecimal();
            System.out.println("输入卖出的股数是：" + inputSaleCount);
            if (inputSaleCount.compareTo(canSaleTotalCount) == 1) {
                System.out.println("输入的卖出股数大于当前可卖出的股数（" + canSaleTotalCount + "），请重新输入" + inputSaleCount);
            }
        } while (inputSaleCount.compareTo(canSaleTotalCount) == 1);

//        System.out.println(new Gson().toJson(purchaseList));

        System.out.println("从一开始买入该股开始计算成本的前提下，这次卖出操作的最终盈利是：" + returnUltimatelyBenefit(purchaseList, inputSalePrice, inputSaleCount));
    }

    private static BigDecimal returnUltimatelyBenefit(List<StockEntity> purchaseList, BigDecimal inputSalePrice, BigDecimal inputSaleCount) {
        List<StockEntity> purchaseListNEW = new ArrayList<>(purchaseList);
        BigDecimal residualCostRecordTotal = new BigDecimal("0");
        BigDecimal residualCountNewTotal = new BigDecimal("0");
//        BigDecimal remainPurchaseCount = new BigDecimal("0");
        BigDecimal inputSaleCountNEW = new BigDecimal(inputSaleCount.toString());
        BigDecimal saleCostTotal = new BigDecimal("0");

        StockEntity stockEntity = new StockEntity();
        do {
            stockEntity = purchaseList.stream()
                    .filter(o -> o.getFlag().equals("是"))
                    .min(((o1, o2) -> o1.getInputPurchasePrice().compareTo(o2.getInputPurchasePrice()))).get();

//            System.out.println(new Gson().toJson(stockEntity));

//            remainPurchaseCount = remainPurchaseCount.add(inputSaleCount.subtract(stockEntity.getInputPurchaseCount()));
//            System.out.println("RemainPurchaseCount:" + remainPurchaseCount);

            inputSaleCount = inputSaleCount.subtract(stockEntity.getInputPurchaseCount());

            if (inputSaleCount.compareTo(BigDecimal.ZERO) == 1) {
                saleCostTotal = saleCostTotal.add(stockEntity.getInputPurchasePrice().multiply(stockEntity.getInputPurchaseCount()));
                stockEntity.setInputPurchaseCount(BigDecimal.ZERO);
                purchaseList.remove(stockEntity);
                purchaseListNEW.remove(stockEntity);
                purchaseListNEW.add(stockEntity);
            } else {
                saleCostTotal = saleCostTotal.add(stockEntity.getInputPurchasePrice().multiply(inputSaleCount.abs()));
                stockEntity.setInputPurchaseCount(inputSaleCount.abs());
                purchaseListNEW.remove(stockEntity);
                purchaseListNEW.add(stockEntity);
            }
        } while (inputSaleCount.compareTo(BigDecimal.ZERO) == 1);

        System.out.println("卖出股票的总价格：" + inputSalePrice.multiply(inputSaleCountNEW).abs());

        System.out.println("卖出股票的原先购入成本：" + saleCostTotal);

        for (int i = 0; i < purchaseListNEW.size(); i++) {
            BigDecimal residualCostRecord = purchaseListNEW.get(i).getInputPurchasePrice().multiply(purchaseListNEW.get(i).getInputPurchaseCount());
            System.out.println("股票" + purchaseListNEW.get(i).getInputPurchasePrice() + "元购入价" +
                    "，剩余" + purchaseListNEW.get(i).getInputPurchaseCount() + "股" +
                    "，购入成本剩余：" + residualCostRecord+(purchaseListNEW.get(i).getFlag().equals("是")?"（历史购入）":"（今日购入）"));
            residualCostRecordTotal = residualCostRecordTotal.add(residualCostRecord);
            residualCountNewTotal = residualCountNewTotal.add(purchaseListNEW.get(i).getInputPurchaseCount());
        }
        System.out.println("剩余股票的总购入成本:" + residualCostRecordTotal);

        System.out.println("剩余股票的最新价值：" + inputSalePrice.multiply(residualCountNewTotal));

        return inputSalePrice.multiply(inputSaleCountNEW).subtract(saleCostTotal);
    }
}
