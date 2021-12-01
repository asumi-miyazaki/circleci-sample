package com.example.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.domain.Coupon;
import com.example.domain.Order;
import com.example.form.DecisionOrderForm;
import com.example.repository.OrderRepository;

/**
 * 注文確定の業務処理を行う.
 * 
 * @author izawamotoki
 *
 */
@Service
public class DecisionOrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private MailSender mailSender;

	/**
	 * 注文確認画面で入力された情報をDBに登録する.
	 * 
	 * @param form 入力された宛先情報
	 * 
	 */
	public void DecisionOrder(DecisionOrderForm form, Coupon coupon) {
		Order order = orderRepository.load(form.getOrderId());
		BeanUtils.copyProperties(form, order);
		if (order.getPaymentMethod() == 1) {
			order.setStatus(1);
		}
		if (order.getPaymentMethod() == 2) {
			order.setStatus(2);
		}
		System.out.println(coupon.getDiscount());
		order.setTotalPrice(order.getTotalPrice() - coupon.getDiscount() + order.getTotalPrice() / 10);
		orderRepository.update(order);
	}

	/**
	 * 注文完了メールを送信する.
	 * 
	 * @param form 入力された宛先情報
	 * 
	 */
	@Async
	public void sendMail(DecisionOrderForm form) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("bass.754522.9@gmail.com");
		mailMessage.setTo(form.getDestinationEmail());
		mailMessage.setSubject("ラクラクヌードル注文確定メール");
		mailMessage.setText("注文が確定しました");

		try {
			this.mailSender.send(mailMessage);
		} catch (MailException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 注文情報を主キー検索する.
	 * 
	 * @param orderId 注文ID
	 * @return 取得された注文情報
	 * 
	 */
	public Order load(Integer orderId) {
		Order order = orderRepository.load(orderId);
		return order;
	}
}
