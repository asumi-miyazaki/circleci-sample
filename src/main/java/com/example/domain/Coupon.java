package com.example.domain;

/**
 * couponテーブルのDomainクラス.
 * 
 * @author inada
 *
 */
public class Coupon {
	/** ID */
	private Integer id;
	/** クーポン名 */
	private String name;
	/** クーポンコード */
	private String code;
	/** 割引料金 */
	private Integer discount;
	/** ユーザーID */
	private Integer userId;
	/** オーダーID */
	private Integer orderId;
	/** 削除フラグ */
	private Boolean deleted;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Coupon [id=" + id + ", name=" + name + ", code=" + code + ", discount=" + discount + ", userId="
				+ userId + ", orderId=" + orderId + ", deleted=" + deleted + "]";
	}
}
