package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.OrderItem;
import com.example.repository.OrderItemRepository;

/**
 * 注文商品テーブルを操作するServiceクラス.
 * 
 * @author izawamotoki
 *
 */
@Service
public class RankingService {

	@Autowired
	private OrderItemRepository orderItemRepository;
	
	/**
	 * 注文回数降順に注文商品情報を取得する.
	 * 
	 * @return 取得された注文商品のリスト
	 */
	public List<OrderItem> ranking() {
		List<OrderItem> orderItemList = orderItemRepository.count();
		return orderItemList;
	}

}
