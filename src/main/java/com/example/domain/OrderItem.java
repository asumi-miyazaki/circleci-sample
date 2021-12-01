package com.example.domain;

import java.util.List;

/**
 * OrderItemドメインクラス.
 * 
 * @author inada
 *
 */
public class OrderItem {
	/** ID */
	private Integer id;
	/** アイテムオブジェクトのID */
	private Integer itemId;
	/** オーダーオブジェクトのID */
	private Integer orderId;
	/** ラーメンの数量 */
	private Integer quantity;
	/** ラーメンのサイズ */
	private Character size;
	/** 商品 */
	private Item item;
	/** トッピングのリスト */
	private List<OrderTopping> orderToppingList;
	/** 注文された回数 */
	private Integer count;

	public void setCharSize(String size) {
		this.size = size.charAt(0);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Character getSize() {
		return size;
	}

	public void setSize(Character size) {
		this.size = size;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public List<OrderTopping> getOrderToppingList() {
		return orderToppingList;
	}

	public void setOrderToppingList(List<OrderTopping> orderToppingList) {
		this.orderToppingList = orderToppingList;
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", itemId=" + itemId + ", orderId=" + orderId + ", quantity=" + quantity
				+ ", size=" + size + ", item=" + item + ", orderToppingList=" + orderToppingList + "]";
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
