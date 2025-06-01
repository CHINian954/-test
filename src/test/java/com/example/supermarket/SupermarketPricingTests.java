package com.example.supermarket;

import com.example.supermarket.cart.ShoppingCart;
import com.example.supermarket.enums.FruitType;
import com.example.supermarket.fruit.Apple;
import com.example.supermarket.fruit.Fruit;
import com.example.supermarket.fruit.Mango;
import com.example.supermarket.fruit.Strawberry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SupermarketPricingTests {
	private Map<FruitType, Fruit> fruits;

	@BeforeEach
	void setUp() {
		fruits = SupermarketPricingSystem.createFruits();
	}

	@Test
	void testCustomerA() {
		ShoppingCart cart = new ShoppingCart(fruits);
		cart.addItem(FruitType.APPLE, 5);      // 5斤苹果
		cart.addItem(FruitType.STRAWBERRY, 3); // 3斤草莓

		double total = cart.calculateTotalPrice();
		double expected = 5 * Apple.BASE_PRICE + 3 * Strawberry.BASE_PRICE;
		assertEquals(expected, total, 0.001, "顾客A测试失败");
	}

	@Test
	void testCustomerB() {
		ShoppingCart cart = new ShoppingCart(fruits);
		cart.addItem(FruitType.APPLE, 5);      // 5斤苹果
		cart.addItem(FruitType.STRAWBERRY, 3);  // 3斤草莓
		cart.addItem(FruitType.MANGO, 2);       // 2斤芒果

		double total = cart.calculateTotalPrice();
		double expected = 5 * Apple.BASE_PRICE + 3 * Strawberry.BASE_PRICE + 2 * Mango.BASE_PRICE;
		assertEquals(expected, total, 0.001, "顾客B测试失败");
	}

	@Test
	void testCustomerC() {
		// 设置草莓8折
		fruits.get(FruitType.STRAWBERRY).setDiscount(0.8);

		ShoppingCart cart = new ShoppingCart(fruits);
		cart.addItem(FruitType.APPLE, 5);      // 5斤苹果
		cart.addItem(FruitType.STRAWBERRY, 3);  // 3斤草莓
		cart.addItem(FruitType.MANGO, 2);       // 2斤芒果

		double total = cart.calculateTotalPrice();
		double expected = 5 * Apple.BASE_PRICE + 3 * Strawberry.BASE_PRICE * 0.8 + 2 * Mango.BASE_PRICE;
		assertEquals(expected, total, 0.001, "顾客C测试失败");
	}

	@Test
	void testCustomerD() {
		// 设置草莓8折
		fruits.get(FruitType.STRAWBERRY).setDiscount(0.8);

		// 测试各种满减边界情况
		testWithAmount(5, 3, 2, 101.2);   // 苹果5斤+草莓3斤+芒果2斤
		testWithAmount(10, 0, 0, 80.0);    // 苹果10斤（80元）
		testWithAmount(0, 8, 0, 83.2);     // 草莓8斤（83.2元）
		testWithAmount(12, 0, 0, 96.0);     // 苹果12斤（96元）
		testWithAmount(12.5, 0, 0, 90.0);   // 苹果12.5斤（100元，满减后90元）
		testWithAmount(13, 0, 0, 94.0);     // 苹果13斤（104元，满减后94元）
		testWithAmount(0, 0, 5, 90.0);      // 芒果5斤（100元，满减后90元）
		testWithAmount(0, 0, 4, 80.0);      // 芒果4斤（80元）
		testWithAmount(0, 0, 4.99, 99.8);   // 芒果4.99斤（99.8元）
		testWithAmount(0, 0, 5.01, 90.2);   // 芒果5.01斤（100.2元，满减后90.2元）
	}

	@Test
	void testNegativeWeight() {
		ShoppingCart cart = new ShoppingCart(fruits);
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> cart.addItem(FruitType.APPLE, -1));
		assertEquals("重量不能为负数: -1.0", exception.getMessage());
	}

	@Test
	void testZeroWeight() {
		ShoppingCart cart = new ShoppingCart(fruits);
		cart.addItem(FruitType.APPLE, 0);
		assertEquals(0, cart.calculateTotalPrice(), 0.001, "零重量测试失败");
	}

	@Test
	void testPromotionDisabled() {
		ShoppingCart cart = new ShoppingCart(fruits);
		cart.setPromotion(false);
		cart.addItem(FruitType.MANGO, 5); // 100元
		assertEquals(100, cart.calculateTotalPrice(), 0.001, "未启用促销测试失败");
	}

	/**
	 * 辅助方法：测试指定重量的水果组合
	 */
	private void testWithAmount(double apple, double strawberry, double mango, double expected) {
		ShoppingCart cart = new ShoppingCart(fruits);
		cart.setPromotion(true); // 启用满减促销

		if (apple > 0) cart.addItem(FruitType.APPLE, apple);
		if (strawberry > 0) cart.addItem(FruitType.STRAWBERRY, strawberry);
		if (mango > 0) cart.addItem(FruitType.MANGO, mango);

		double total = cart.calculateTotalPrice();
		assertEquals(expected, total, 0.001,
				String.format("重量测试失败 [苹果:%.2f, 草莓:%.2f, 芒果:%.2f]", apple, strawberry, mango));
	}
}