package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Order;
import com.example.repository.OrderRepository;

@Service
public class OrderHistoryService {

	@Autowired
	private OrderRepository orderRepository;

	/**
	 * ユーザIDで過去に注文した商品を検索する
	 * 
	 * @param userId ログインしているユーザID
	 * @return 検索した過去の注文リスト
	 */
	public List<Order> SearchAfterOrderUserId(int userId) {
		List<Order> orderList = orderRepository.findByUserIdButAfterOrder(userId);
		return orderList;
	}

	/**
	 * OrderIdで注文した商品詳細を取得する.
	 * 
	 * @param userId  ログインしているユーザId
	 * @param orderId 検索したい注文履歴
	 * @return 検索した注文履歴
	 */
	public Order searchOrderIdAndCheckUserId(int userId, int orderId) {
		Order order = orderRepository.load(orderId);
		if (userId != order.getUserId()) {
			order = null;
		}
		return order;
	}
}
