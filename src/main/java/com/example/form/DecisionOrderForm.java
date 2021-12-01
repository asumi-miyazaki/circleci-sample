package com.example.form;

import java.sql.Timestamp;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 入力された宛先情報を表すフォーム.
 * 
 * @author izawamotoki
 *
 */
public class DecisionOrderForm {

	/** 注文ID */
	private Integer orderId;

	/** 注文日 */
	private Date orderDate;

	/** 宛先氏名 */
	@NotBlank(message = "名前を入力してください")
	private String destinationName;

	/** 宛先Eメール */
	@Email(message = "メールアドレスの形式ではありません")
	@NotBlank(message = "メールアドレスを入力してください")
	private String destinationEmail;

	/** 宛先郵便番号 */
	@Pattern(regexp = "^[0-9]{3}-[0-9]{4}$", message = "郵便番号はXXX-XXXXの形式で入力してください")
	private String destinationZipcode;

	/** 宛先住所 */
	@NotBlank(message = "住所を入力してください")
	private String destinationAddress;

	/** 宛先TEL */
	@Pattern(regexp = "^[0-9]{3,4}-[0-9]{4}-[0-9]{4}$", message = "電話番号はXXXX-XXXX-XXXXの形式で入力してください")
	private String destinationTel;

	/** 配達日時 */
	private Timestamp deliveryTime;

	/** 配達日 */
	@NotBlank(message = "配達日時を入力してください")
	private String deliveryTimeDate;

	/** 配達時間 */
	@NotBlank(message = "配達日時を入力してください")
	private String deliveryTimeHour;

	/** 支払方法 */
	private Integer paymentMethod;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public String getDestinationEmail() {
		return destinationEmail;
	}

	public void setDestinationEmail(String destinationEmail) {
		this.destinationEmail = destinationEmail;
	}

	public String getDestinationZipcode() {
		return destinationZipcode;
	}

	public void setDestinationZipcode(String destinationZipcode) {
		this.destinationZipcode = destinationZipcode;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public String getDestinationTel() {
		return destinationTel;
	}

	public void setDestinationTel(String destinationTel) {
		this.destinationTel = destinationTel;
	}

	public Timestamp getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Timestamp deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getDeliveryTimeDate() {
		return deliveryTimeDate;
	}

	public void setDeliveryTimeDate(String deliveryTimeDate) {
		this.deliveryTimeDate = deliveryTimeDate;
	}

	public String getDeliveryTimeHour() {
		return deliveryTimeHour;
	}

	public void setDeliveryTimeHour(String deliveryTimeHour) {
		this.deliveryTimeHour = deliveryTimeHour;
	}

	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}