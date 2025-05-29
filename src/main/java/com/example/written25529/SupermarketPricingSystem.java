package com.example.written25529;

import java.util.HashMap;
import java.util.Map;

public class SupermarketPricingSystem {

	// 水果类型枚举，表示超市销售的水果种类
	public enum FruitType {
		APPLE,   // 苹果
		STRAWBERRY, // 草莓
		MANGO    // 芒果
	}

	/**
	 * 抽象水果类，定义了水果的基本属性和行为
	 */
	static abstract class Fruit {
		private final double pricePerKg; // 水果每公斤价格
		private double discount;        // 当前折扣率（1.0表示无折扣）

		/**
		 * 水果类构造函数
		 * @param pricePerKg 水果每公斤价格
		 */
		public Fruit(double pricePerKg) {
			this.pricePerKg = pricePerKg;
			this.discount = 1.0; // 默认不打折
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

	/**
	 * 苹果类，继承自水果基类
	 */
	static class Apple extends Fruit {
		public static final double BASE_PRICE = 8.0; // 苹果基础价格

		public Apple() {
			super(BASE_PRICE); // 调用基类构造函数
		}
	}

	/**
	 * 草莓类，继承自水果基类
	 */
	static class Strawberry extends Fruit {
		public static final double BASE_PRICE = 13.0; // 草莓基础价格

		public Strawberry() {
			super(BASE_PRICE); // 调用基类构造函数
		}
	}

	/**
	 * 芒果类，继承自水果基类
	 */
	static class Mango extends Fruit {
		public static final double BASE_PRICE = 20.0; // 芒果基础价格

		public Mango() {
			super(BASE_PRICE); // 调用基类构造函数
		}
	}

	/**
	 * 购物车类，用于管理顾客选购的水果和计算总价
	 */
	static class ShoppingCart {
		private final Map<FruitType, Fruit> fruits; // 水果类型与水果对象的映射
		private final Map<FruitType, Double> items = new HashMap<>(); // 购物车中的商品（水果类型和重量）
		private boolean hasPromotion = false;       // 是否启用满减促销

		// 使用常量替代字段
		private static final double DISCOUNT_THRESHOLD = 100.0;   // 满减门槛金额
		private static final double DISCOUNT_AMOUNT = 10.0;       // 满减优惠金额

		/**
		 * 购物车构造函数
		 * @param fruits 超市提供的水果集合
		 */
		public ShoppingCart(Map<FruitType, Fruit> fruits) {
			this.fruits = fruits;
		}

		/**
		 * 向购物车添加水果
		 * @param fruitType 水果类型
		 * @param weight 水果重量（公斤）
		 */
		public void addItem(FruitType fruitType, double weight) {
			if (weight < 0) {
				throw new IllegalArgumentException("重量不能为负数: " + weight);
			}
			// 累加同类型水果的重量
			items.put(fruitType, items.getOrDefault(fruitType, 0.0) + weight);
		}

		/**
		 * 设置是否启用满减促销
		 * @param hasPromotion true启用满减，false不启用
		 */
		public void setPromotion(boolean hasPromotion) {
			this.hasPromotion = hasPromotion;
		}

		/**
		 * 计算购物车总价
		 * @return 总金额（已应用折扣和促销）
		 */
		public double calculateTotalPrice() {
			double total = 0.0;

			// 遍历购物车中所有商品
			for (Map.Entry<FruitType, Double> entry : items.entrySet()) {
				Fruit fruit = fruits.get(entry.getKey());
				if (fruit != null) {
					// 累加每种水果的价格
					total += fruit.calculatePrice(entry.getValue());
				}
			}

			// 如果启用促销且达到门槛金额，应用满减优惠
			if (hasPromotion && total >= DISCOUNT_THRESHOLD) {
				total -= DISCOUNT_AMOUNT;
			}

			return total;
		}
	}

	/**
	 * 主方法，执行测试用例
	 */
	public static void main(String[] args) {
		// 初始化水果集合
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

	/**
	 * 测试顾客A场景（无芒果，无折扣）
	 * @param fruits 水果集合
	 */
	private static void testCustomerA(Map<FruitType, Fruit> fruits) {
		ShoppingCart cart = new ShoppingCart(fruits);
		cart.addItem(FruitType.APPLE, 5);      // 5斤苹果
		cart.addItem(FruitType.STRAWBERRY, 3); // 3斤草莓

		double total = cart.calculateTotalPrice();
		double expected = 5 * Apple.BASE_PRICE + 3 * Strawberry.BASE_PRICE;
		System.out.println("顾客A测试: " + (Math.abs(total - expected) < 0.001 ? "通过" : "失败"));
		System.out.printf("实际金额: %.2f, 预期金额: %.2f%n%n", total, expected);
	}

	/**
	 * 测试顾客B场景（增加芒果）
	 * @param fruits 水果集合
	 */
	private static void testCustomerB(Map<FruitType, Fruit> fruits) {
		ShoppingCart cart = new ShoppingCart(fruits);
		cart.addItem(FruitType.APPLE, 5);      // 5斤苹果
		cart.addItem(FruitType.STRAWBERRY, 3);  // 3斤草莓
		cart.addItem(FruitType.MANGO, 2);       // 2斤芒果

		double total = cart.calculateTotalPrice();
		double expected = 5 * Apple.BASE_PRICE + 3 * Strawberry.BASE_PRICE + 2 * Mango.BASE_PRICE;
		System.out.println("顾客B测试: " + (Math.abs(total - expected) < 0.001 ? "通过" : "失败"));
		System.out.printf("实际金额: %.2f, 预期金额: %.2f%n%n", total, expected);
	}

	/**
	 * 测试顾客C场景（草莓8折）
	 * @param fruits 水果集合
	 */
	private static void testCustomerC(Map<FruitType, Fruit> fruits) {
		// 设置草莓8折优惠
		fruits.get(FruitType.STRAWBERRY).setDiscount(0.8);

		ShoppingCart cart = new ShoppingCart(fruits);
		cart.addItem(FruitType.APPLE, 5);      // 5斤苹果
		cart.addItem(FruitType.STRAWBERRY, 3);  // 3斤草莓
		cart.addItem(FruitType.MANGO, 2);       // 2斤芒果

		double total = cart.calculateTotalPrice();
		double expected = 5 * Apple.BASE_PRICE + 3 * Strawberry.BASE_PRICE * 0.8 + 2 * Mango.BASE_PRICE;
		System.out.println("顾客C测试: " + (Math.abs(total - expected) < 0.001 ? "通过" : "失败"));
		System.out.printf("实际金额: %.2f, 预期金额: %.2f%n%n", total, expected);

		// 重置草莓折扣为原价
		fruits.get(FruitType.STRAWBERRY).setDiscount(1.0);
	}

	/**
	 * 测试顾客D场景（草莓8折 + 满100减10）
	 * @param fruits 水果集合
	 */
	private static void testCustomerD(Map<FruitType, Fruit> fruits) {
		// 设置草莓8折优惠
		fruits.get(FruitType.STRAWBERRY).setDiscount(0.8);

		// 测试满减边界值
		testWithAmount(fruits, 5, 3, 2, 101.2);   // 苹果5斤 + 草莓3斤 + 芒果2斤
		testWithAmount(fruits, 10, 0, 0, 80.0);    // 苹果10斤（80元）
		testWithAmount(fruits, 0, 8, 0, 83.2);     // 草莓8斤（83.2元）
		testWithAmount(fruits, 12, 0, 0, 96.0);     // 苹果12斤（96元）
		testWithAmount(fruits, 12.5, 0, 0, 90.0);   // 苹果12.5斤（100元，满减后90元）
		testWithAmount(fruits, 13, 0, 0, 94.0);     // 苹果13斤（104元，满减后94元）
		testWithAmount(fruits, 0, 0, 5, 90.0);      // 芒果5斤（100元，满减后90元）
		testWithAmount(fruits, 0, 0, 4, 80.0);      // 芒果4斤（80元）
		testWithAmount(fruits, 0, 0, 4.99, 99.8);   // 芒果4.99斤（99.8元）
		testWithAmount(fruits, 0, 0, 5.01, 90.2);   // 芒果5.01斤（100.2元，满减后90.2元）

		// 重置草莓折扣为原价
		fruits.get(FruitType.STRAWBERRY).setDiscount(1.0);
	}

	/**
	 * 辅助方法：测试指定重量的水果组合
	 * @param fruits 水果集合
	 * @param appleWeight 苹果重量
	 * @param strawberryWeight 草莓重量
	 * @param mangoWeight 芒果重量
	 * @param expected 预期金额
	 */
	private static void testWithAmount(Map<FruitType, Fruit> fruits,
									   double appleWeight,
									   double strawberryWeight,
									   double mangoWeight,
									   double expected) {
		ShoppingCart cart = new ShoppingCart(fruits);
		cart.setPromotion(true); // 启用满减促销

		// 添加指定重量的水果
		if (appleWeight > 0) cart.addItem(FruitType.APPLE, appleWeight);
		if (strawberryWeight > 0) cart.addItem(FruitType.STRAWBERRY, strawberryWeight);
		if (mangoWeight > 0) cart.addItem(FruitType.MANGO, mangoWeight);

		// 计算并验证总价
		double total = cart.calculateTotalPrice();
		boolean pass = Math.abs(total - expected) < 0.001;
		System.out.printf("顾客D测试[苹果:%.2f斤, 草莓:%.2f斤, 芒果:%.2f斤]: %s%n",
				appleWeight, strawberryWeight, mangoWeight,
				pass ? "通过" : "失败");
		System.out.printf("实际金额: %.2f元, 预期金额: %.2f元%n%n", total, expected);
	}
}