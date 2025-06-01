package com.example.supermarket;

import com.example.supermarket.cart.ShoppingCart;
import com.example.supermarket.enums.FruitType;
import com.example.supermarket.fruit.Apple;
import com.example.supermarket.fruit.Fruit;
import com.example.supermarket.fruit.Mango;
import com.example.supermarket.fruit.Strawberry;

import java.util.HashMap;
import java.util.Map;

/**
 * 超市计价系统入口
 */
public class SupermarketPricingSystem {
	public static void main(String[] args) {
		// 测试使用JUnit
		System.out.println("请运行JUnit测试验证功能");
	}

	/**
	 * 创建基础水果集合
	 */
	public static Map<FruitType, Fruit> createFruits() {
		Map<FruitType, Fruit> fruits = new HashMap<>();
		fruits.put(FruitType.APPLE, new Apple());
		fruits.put(FruitType.STRAWBERRY, new Strawberry());
		fruits.put(FruitType.MANGO, new Mango());
		return fruits;
	}
}