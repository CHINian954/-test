package com.example.written25529;

import java.util.HashMap;
import java.util.Map;

/**
 * 水果超市计价系统
 */
public class SupermarketPricingSystem {

	// 水果类型枚举
	public enum FruitType {
		APPLE, STRAWBERRY, MANGO
	}

	// 基础水果类
	static abstract class Fruit {
		private final double pricePerKg;
		private double discount;

		public Fruit(double pricePerKg) {
			this.pricePerKg = pricePerKg;
			this.discount = 1.0; // 默认不打折
		}

		public double getPricePerKg() {
			return pricePerKg;
		}

		public void setDiscount(double discount) {
			this.discount = discount;
		}

		public double calculatePrice(double weight) {
			return pricePerKg * weight * discount;
		}
	}

	// 苹果类
	static class Apple extends Fruit {
		public static final double BASE_PRICE = 8.0;

		public Apple() {
			super(BASE_PRICE);
		}
	}

	// 草莓类
	static class Strawberry extends Fruit {
		public static final double BASE_PRICE = 13.0;

		public Strawberry() {
			super(BASE_PRICE);
		}
	}

	// 芒果类
	static class Mango extends Fruit {
		public static final double BASE_PRICE = 20.0;

		public Mango() {
			super(BASE_PRICE);
		}
	}

	// 购物车类
	static class ShoppingCart {
		private final Map<FruitType, Fruit> fruits;
		private final Map<FruitType, Double> items = new HashMap<>();
		private boolean hasPromotion = false;
		private double discountThreshold = 100.0;
		private double discountAmount = 10.0;

		public ShoppingCart(Map<FruitType, Fruit> fruits) {
			this.fruits = fruits;
		}

		public void addItem(FruitType fruitType, double weight) {
			if (weight < 0) {
				throw new IllegalArgumentException("重量不能为负数: " + weight);
			}
			items.put(fruitType, items.getOrDefault(fruitType, 0.0) + weight);
		}

		public void setPromotion(boolean hasPromotion) {
			this.hasPromotion = hasPromotion;
		}

		public double calculateTotalPrice() {
			double total = 0.0;

			for (Map.Entry<FruitType, Double> entry : items.entrySet()) {
				Fruit fruit = fruits.get(entry.getKey());
				if (fruit != null) {
					total += fruit.calculatePrice(entry.getValue());
				}
			}

			// 应用满减促销
			if (hasPromotion && total >= discountThreshold) {
				total -= discountAmount;
			}

			return total;
		}
	}

	// 测试用例
	public static void main(String[] args) {
		// 初始化水果
		Map<FruitType, Fruit> fruits = new HashMap<>();
		fruits.put(FruitType.APPLE, new Apple());
		fruits.put(FruitType.STRAWBERRY, new Strawberry());
		fruits.put(FruitType.MANGO, new Mango());

		// 测试顾客A（只有苹果和草莓）
		testCustomerA(fruits);

		// 测试顾客B（增加芒果）
		testCustomerB(fruits);

		// 测试顾客C（草莓8折）
		testCustomerC(fruits);

		// 测试顾客D（满100减10）
		testCustomerD(fruits);
	}

	private static void testCustomerA(Map<FruitType, Fruit> fruits) {
		ShoppingCart cart = new ShoppingCart(fruits);
		cart.addItem(FruitType.APPLE, 5);  // 5斤苹果
		cart.addItem(FruitType.STRAWBERRY, 3); // 3斤草莓

		double total = cart.calculateTotalPrice();
		double expected = 5 * Apple.BASE_PRICE + 3 * Strawberry.BASE_PRICE;
		System.out.println("顾客A测试: " + (Math.abs(total - expected) < 0.001 ? "通过" : "失败"));
		System.out.printf("实际金额: %.2f, 预期金额: %.2f%n", total, expected);
	}

	private static void testCustomerB(Map<FruitType, Fruit> fruits) {
		ShoppingCart cart = new ShoppingCart(fruits);
		cart.addItem(FruitType.APPLE, 5);  // 5斤苹果
		cart.addItem(FruitType.STRAWBERRY, 3); // 3斤草莓
		cart.addItem(FruitType.MANGO, 2); // 2斤芒果

		double total = cart.calculateTotalPrice();
		double expected = 5 * Apple.BASE_PRICE + 3 * Strawberry.BASE_PRICE + 2 * Mango.BASE_PRICE;
		System.out.println("顾客B测试: " + (Math.abs(total - expected) < 0.001 ? "通过" : "失败"));
		System.out.printf("实际金额: %.2f, 预期金额: %.2f%n", total, expected);
	}

	private static void testCustomerC(Map<FruitType, Fruit> fruits) {
		// 设置草莓8折
		((Strawberry) fruits.get(FruitType.STRAWBERRY)).setDiscount(0.8);

		ShoppingCart cart = new ShoppingCart(fruits);
		cart.addItem(FruitType.APPLE, 5);  // 5斤苹果
		cart.addItem(FruitType.STRAWBERRY, 3); // 3斤草莓
		cart.addItem(FruitType.MANGO, 2); // 2斤芒果

		double total = cart.calculateTotalPrice();
		double expected = 5 * Apple.BASE_PRICE + 3 * Strawberry.BASE_PRICE * 0.8 + 2 * Mango.BASE_PRICE;
		System.out.println("顾客C测试: " + (Math.abs(total - expected) < 0.001 ? "通过" : "失败"));
		System.out.printf("实际金额: %.2f, 预期金额: %.2f%n", total, expected);

		// 重置草莓折扣
		((Strawberry) fruits.get(FruitType.STRAWBERRY)).setDiscount(1.0);
	}

	private static void testCustomerD(Map<FruitType, Fruit> fruits) {
		// 设置草莓8折
		((Strawberry) fruits.get(FruitType.STRAWBERRY)).setDiscount(0.8);

		// 测试满减边界值
		testWithAmount(fruits, 5, 3, 2, 142.0); // 5苹果+3草莓+2芒果=152-10=142
		testWithAmount(fruits, 10, 0, 0, 80.0); // 80元不满减
		testWithAmount(fruits, 0, 8, 0, 83.2); // 83.2元不满减
		testWithAmount(fruits, 12, 0, 0, 96.0); // 96元不满减
		testWithAmount(fruits, 12.5, 0, 0, 100.0); // 100元满减后90
		testWithAmount(fruits, 13, 0, 0, 94.0); // 104-10=94
		testWithAmount(fruits, 0, 0, 5, 90.0); // 100元满减后90
		testWithAmount(fruits, 0, 0, 4, 80.0); // 80元不满减
		testWithAmount(fruits, 0, 0, 4.99, 99.8); // 99.8元不满减
		testWithAmount(fruits, 0, 0, 5.01, 90.2); // 100.2-10=90.2

		// 重置草莓折扣
		((Strawberry) fruits.get(FruitType.STRAWBERRY)).setDiscount(1.0);
	}

	private static void testWithAmount(Map<FruitType, Fruit> fruits,
									   double appleWeight,
									   double strawberryWeight,
									   double mangoWeight,
									   double expected) {
		ShoppingCart cart = new ShoppingCart(fruits);
		cart.setPromotion(true); // 启用满减

		if (appleWeight > 0) cart.addItem(FruitType.APPLE, appleWeight);
		if (strawberryWeight > 0) cart.addItem(FruitType.STRAWBERRY, strawberryWeight);
		if (mangoWeight > 0) cart.addItem(FruitType.MANGO, mangoWeight);

		double total = cart.calculateTotalPrice();
		boolean pass = Math.abs(total - expected) < 0.001;
		System.out.printf("顾客D测试[苹果:%.2f, 草莓:%.2f, 芒果:%.2f]: %s%n",
				appleWeight, strawberryWeight, mangoWeight,
				pass ? "通过" : "失败");
		System.out.printf("实际金额: %.2f, 预期金额: %.2f%n", total, expected);
	}
}