package com.example.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Order;
import com.example.domain.User;
import com.example.repository.OrderRepository;

/**
 * 注文確認画面の業務処理を行う.
 * 
 * @author izawamotoki
 *
 */
@Service
public class ConfirmOrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private HttpSession session;

	/**
	 * 注文確認画面に表示する商品一覧を取得する.
	 * 
	 * @param orderId を取得したい注文のID
	 * @return 取得した注文情報
	 */
	public Order confirmOrder(Integer orderId) {
		Order order = orderRepository.load(orderId);
		User user = (User) session.getAttribute("user");
		order.setUserId(user.getId());
		orderRepository.update(order);
		return order;
	}
}
