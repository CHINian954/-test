package com.example.supermarket.cart;

import com.example.supermarket.enums.FruitType;
import com.example.supermarket.fruit.Fruit;

import java.util.HashMap;
import java.util.Map;

/**
 * 购物车类
 */
public class ShoppingCart {
    private final Map<FruitType, Fruit> fruits;
    private final Map<FruitType, Double> items = new HashMap<>();
    private boolean hasPromotion = false;

    // 满减规则常量
    private static final double DISCOUNT_THRESHOLD = 100.0;
    private static final double DISCOUNT_AMOUNT = 10.0;

    public ShoppingCart(Map<FruitType, Fruit> fruits) {
        this.fruits = fruits;
    }

    /**
     * 添加水果到购物车
     * @param fruitType 水果类型
     * @param weight 重量（公斤）
     */
    public void addItem(FruitType fruitType, double weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("重量不能为负数: " + weight);
        }
        items.put(fruitType, items.getOrDefault(fruitType, 0.0) + weight);
    }

    /**
     * 设置是否启用满减促销
     * @param hasPromotion true启用，false不启用
     */
    public void setPromotion(boolean hasPromotion) {
        this.hasPromotion = hasPromotion;
    }

    /**
     * 计算购物车总价
     */
    public double calculateTotalPrice() {
        double total = 0.0;

        for (Map.Entry<FruitType, Double> entry : items.entrySet()) {
            Fruit fruit = fruits.get(entry.getKey());
            if (fruit != null) {
                total += fruit.calculatePrice(entry.getValue());
            }
        }

        // 应用满减促销
        if (hasPromotion && total >= DISCOUNT_THRESHOLD) {
            total -= DISCOUNT_AMOUNT;
        }

        return total;
    }
}