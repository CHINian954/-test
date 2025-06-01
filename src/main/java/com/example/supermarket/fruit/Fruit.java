package com.example.supermarket.fruit;

/**
 * 抽象水果类
 */
public abstract class Fruit {
    private final double pricePerKg; // 每公斤价格
    private double discount = 1.0;   // 折扣率（默认1.0，不打折）

    protected Fruit(double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    /**
     * 设置折扣率
     * @param discount 折扣率（0.8表示8折）
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * 计算指定重量的水果价格
     * @param weight 水果重量（公斤）
     * @return 计算后的价格
     */
    public double calculatePrice(double weight) {
        return pricePerKg * weight * discount;
    }
}