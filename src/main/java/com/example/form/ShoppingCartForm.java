package com.example.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

/**
 * ショッピングカートに商品を追加する際のFormクラス.
 * 
 * @author inada
 *
 */
public class ShoppingCartForm {
	/** 商品ID */
	private String itemId;
	/** 商品個数 */
	private String quantity;
	/** 商品の大きさ */
	@NotEmpty(message="サイズを選択してください")
	private String size;
	/** トッピングの数 */
	private List<String> toppingList;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public List<String> getToppingList() {
		return toppingList;
	}

	public void setToppingList(List<String> toppingList) {
		this.toppingList = toppingList;
	}

	@Override
	public String toString() {
		return "ShoppingCartForm [itemId=" + itemId + ", quantity=" + quantity + ", size=" + size + ", toppingList="
				+ toppingList + "]";
	}
}
