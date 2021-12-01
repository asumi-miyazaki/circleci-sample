package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;
import com.example.domain.User;
import com.example.form.ShoppingCartForm;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.OrderToppingRepository;
import com.example.repository.ToppingRepository;

/**
 * ショッピングカートの業務処理を行う.
 * 
 * @author inada
 *
 */
@Service
public class ShoppingCartService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderToppingRepository orderToppingRepository;

	@Autowired
	private ToppingRepository toppingRepository;

	/**
	 * ショッピングカートの商品を取得する.
	 * 
	 * @param userId ショッピングカートを取得したい人のID
	 * @return 取得したショッピングカートのリスト
	 */
	public Order showItemShoppingCart(int userId) {
		int status = 0;
		int totalPrice = 0;
		Order order = orderRepository.findByUserIdAndStatus(userId, status);
		if (order.getId() != null) {
			for (OrderItem orderItem : order.getOrderItemList()) {
				for (int i = 0; i < orderItem.getQuantity(); i++) {
					if (orderItem.getSize() == 'M') {
						totalPrice += orderItem.getItem().getPriceM();
						if (orderItem.getOrderToppingList() != null) {
							for (OrderTopping orderTopping : orderItem.getOrderToppingList()) {
								if (orderTopping.getTopping().getId() != null) {
									totalPrice += orderTopping.getTopping().getPriceM();
								}
							}
						}
					} else if (orderItem.getSize() == 'L') {
						totalPrice += orderItem.getItem().getPriceL();
						if (orderItem.getOrderToppingList() != null) {
							for (OrderTopping orderTopping : orderItem.getOrderToppingList()) {
								if (orderTopping.getTopping().getId() != null) {
									totalPrice += orderTopping.getTopping().getPriceL();
								}
							}
						}
					}
				}
			}
		}
		order.setTotalPrice(totalPrice);
		orderRepository.update(order);
		return order;
	}

	/**
	 * ショッピングカートに商品を追加する.
	 * 
	 * @param form   登録する商品情報
	 * @param userId 登録するショッピングカートを検索するときに使用
	 */
	public void addShoppingCart(ShoppingCartForm form, int userId) {
		int status = 0;
		Order order = orderRepository.findByUserIdAndStatus(userId, status);
		if (order.getId() == null) {
			order.setUserId(userId);
			order.setStatus(status);
			order.setTotalPrice(0);
			order = orderRepository.insert(order);
		}
		OrderItem orderItem = new OrderItem();
		orderItem.setItemId(Integer.parseInt(form.getItemId()));
		orderItem.setOrderId(order.getId());
		orderItem.setQuantity(Integer.parseInt(form.getQuantity()));
		orderItem.setCharSize(form.getSize());
		orderItem = orderItemRepository.insert(orderItem);
		if (form.getToppingList() != null) {
			for (String toppingId : form.getToppingList()) {
				Topping topping = toppingRepository.load(Integer.parseInt(toppingId));
				OrderTopping orderTopping = new OrderTopping();
				orderTopping.setToppingId(topping.getId());
				orderTopping.setOrderItemId(orderItem.getId());
				orderToppingRepository.insert(orderTopping);
			}
		}
	}

	/**
	 * ショッピングカート内の商品を削除する.
	 * 
	 * @param orderItemId 削除したい商品
	 */
	public void deleteItemShoppingCart(int orderItemId) {
		orderItemRepository.deleteOrderItemWithOrderTopping(orderItemId);
	}

	/**
	 * 仮IDで登録しているオーダをログイン後のオーダに変更する.
	 * 
	 * @param order 仮IDで登録している状態のオーダ
	 * @param user  ログインしたUser情報
	 */
	public void updateTemporalUserIdToUserId(User temporalUser, User user) {
		int status = 0;
		Order userOrder = orderRepository.findByUserIdAndStatus(user.getId(), status);
		if (userOrder.getId() != null) {
			Order temporalOrder = orderRepository.findByUserIdAndStatus(temporalUser.getId(), status);
			for (OrderItem orderItem : temporalOrder.getOrderItemList()) {
				orderItem.setOrderId(userOrder.getId());
				orderItemRepository.update(orderItem);
			}
			orderRepository.delete(temporalOrder.getId());
		} else {
			Order temporalOrder = orderRepository.findByUserIdAndStatus(temporalUser.getId(), status);
			temporalOrder.setUserId(user.getId());
			orderRepository.update(temporalOrder);
		}
	}
}
