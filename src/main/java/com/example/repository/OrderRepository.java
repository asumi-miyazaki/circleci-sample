package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.domain.Item;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;

/**
 * ordersテーブルを操作するRepositoryクラス
 * 
 * @author inada
 *
 */
@Repository
public class OrderRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final String ITEMTABLE = "items";
	private static final String TOPPINGTABLE = "toppings";
	private static final String ORDERTABLE = "orders";
	private static final String ORDERITEMTABLE = "order_items";
	private static final String ORDERTOPPINGTABLE = "order_toppings";

	private static final RowMapper<Order> ORDER_ROW_MAPPER = new BeanPropertyRowMapper<>(Order.class);

	private static final ResultSetExtractor<Order> RSE_ORDER = (rs) -> {
		Order order = new Order();
		List<OrderItem> orderItemList = null;
		List<OrderTopping> orderToppingList = null;
		List<Topping> toppingList = null;
		OrderItem orderItem = null;
		OrderTopping orderTopping = null;
		Item item = null;
		int beforeOrderId = 0;
		int beforeOrderItemId = 0;
		int beforeOrderToppingId = 0;
		int beforeItemId = 0;
		int beforeToppingId = 0;

		while (rs.next()) {
			int nowOrderId = rs.getInt("orderId");
			int nowOrderItemId = rs.getInt("orderitemId");
			int nowOrderToppingId = rs.getInt("orderToppingId");
			int nowItemId = rs.getInt("itemId");
			int nowToppingId = rs.getInt("toppingId");

			if (nowOrderId != beforeOrderId) {
				order.setId(rs.getInt("orderId"));
				order.setUserId(rs.getInt("orderUserId"));
				order.setStatus(rs.getInt("orderStatus"));
				order.setTotalPrice(rs.getInt("orderTotalPrice"));
				order.setOrderDate(rs.getDate("orderOrderDate"));
				order.setDestinationName(rs.getString("orderDestinationName"));
				order.setDestinationEmail(rs.getString("orderDestinationEmail"));
				order.setDestinationZipcode(rs.getString("orderDestinationZipcode"));
				order.setDestinationAddress(rs.getString("orderDestinationAddress"));
				order.setDestinationTel(rs.getString("orderDestinationTel"));
				order.setDeliveryTime(rs.getTimestamp("orderDeliveryTime"));
				order.setPaymentMethod(rs.getInt("orderPaymentMethod"));
				orderItemList = new ArrayList<OrderItem>();
				order.setOrderItemList(orderItemList);
			}

			if (rs.getInt("orderItemId") != 0) {
				if (nowOrderItemId != beforeOrderItemId) {
					orderItem = new OrderItem();
					orderItem.setId(rs.getInt("orderItemId"));
					orderItem.setItemId(rs.getInt("orderItemItemId"));
					orderItem.setOrderId(rs.getInt("orderItemOrderId"));
					orderItem.setQuantity(rs.getInt("orderItemQuantity"));
					orderItem.setCharSize(rs.getString("orderItemSize"));
					orderItem.setItem(item);
					orderToppingList = new ArrayList<OrderTopping>();
					orderItem.setOrderToppingList(orderToppingList);
					orderItemList.add(orderItem);
					orderToppingList = new ArrayList<OrderTopping>();
					orderItem.setOrderToppingList(orderToppingList);
				}

				if (nowItemId != beforeItemId) {
					item = new Item();
					item.setId(rs.getInt("itemId"));
					item.setName(rs.getString("itemName"));
					item.setDescription(rs.getString("itemDescription"));
					item.setPriceM(rs.getInt("itemPriceM"));
					item.setPriceL(rs.getInt("itemPriceL"));
					item.setImagePath(rs.getString("itemImagePath"));
					item.setDeleted(rs.getBoolean("itemDeleted"));
					toppingList = new ArrayList<Topping>();
					item.setToppingList(toppingList);

					orderItem.setItem(item);
				}

				if (rs.getInt("orderToppingId") != 0) {
					Topping topping = new Topping();
					if (nowOrderToppingId != beforeOrderToppingId) {
						orderTopping = new OrderTopping();
						orderTopping.setId(rs.getInt("orderToppingId"));
						orderTopping.setToppingId(rs.getInt("orderToppingToppingId"));
						orderTopping.setOrderItemId(rs.getInt("orderToppingOrderItemId"));
						orderTopping.setTopping(topping);
						orderToppingList.add(orderTopping);
					}

					if (nowToppingId != beforeToppingId) {
						topping.setId(rs.getInt("toppingId"));
						topping.setName(rs.getString("toppingName"));
						topping.setPriceM(rs.getInt("toppingPriceM"));
						topping.setPriceL(rs.getInt("toppingPriceL"));
						toppingList.add(topping);
					}
				}
			}

			beforeOrderId = nowOrderId;
			beforeOrderItemId = nowOrderItemId;
			beforeOrderToppingId = nowOrderToppingId;
			beforeItemId = nowItemId;
			beforeToppingId = nowToppingId;
		}
		return order;
	};

	/**
	 * ショッピングカート内の商品を全て検索する.
	 * 
	 * @param userId 検索したいショッピングカート
	 * @param status 注文の状態
	 * @return 検索した注文商品のリスト
	 */
	public Order findByUserIdAndStatus(int userId, int status) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT orders.id as orderId, orders.user_id as orderUserId, orders.status as orderStatus, orders.total_price as orderTotalPrice, orders.order_date as orderOrderDate, orders.destination_name as orderDestinationName, orders.destination_email as orderDestinationEmail, orders.destination_zipcode as orderDestinationZipcode, orders.destination_address as orderDestinationAddress, orders.destination_tel as orderDestinationTel, orders.delivery_time as orderDeliveryTime, orders.payment_method as orderPaymentMethod, ");
		sql.append(
				"orderItems.id as orderItemId, orderItems.item_id as orderItemItemId, orderItems.order_id as orderItemOrderId, orderItems.quantity as orderItemQuantity, orderItems.size as orderItemSize, ");
		sql.append(
				"orderToppings.id as orderToppingId, orderToppings.topping_id as orderToppingToppingId, orderToppings.order_item_id as orderToppingOrderItemId, ");
		sql.append(
				"items.id as itemId, items.name as itemName, items.description as itemDescription, items.price_m as itemPriceM, items.price_l as itemPriceL, items.image_path as itemImagePath, items.deleted as itemDeleted, ");
		sql.append(
				"toppings.id as toppingId, toppings.name as toppingName, toppings.price_m as toppingPriceM, toppings.price_l as toppingPriceL ");
		sql.append("FROM orders as " + ORDERTABLE + " ");
		sql.append("LEFT OUTER JOIN " + ORDERITEMTABLE + " as orderItems ON orders.id = orderItems.order_id ");
		sql.append("LEFT OUTER JOIN " + ORDERTOPPINGTABLE
				+ " as orderToppings ON orderItems.id = orderToppings.order_item_id ");
		sql.append("LEFT OUTER JOIN " + ITEMTABLE + " as items ON orderItems.item_id = items.id ");
		sql.append("LEFT OUTER JOIN " + TOPPINGTABLE + " as toppings ON orderToppings.topping_id = toppings.id ");
		sql.append("WHERE orders.user_id=:userId AND orders.status=:status ORDER BY orderItems.id ASC;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("status", status);
		Order order = template.query(sql.toString(), param, RSE_ORDER);
		return order;
	}

	/**
	 * 注文詳細を作成する.
	 * 
	 * @param order 注文詳細情報
	 * @return 作成した注文詳細
	 */
	public Order insert(Order order) {
		String sql = "insert into orders (user_id,status,total_price) values(:userId,:status,:totalPrice);";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", order.getUserId())
				.addValue("status", order.getStatus()).addValue("totalPrice", order.getTotalPrice());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnNames = { "id" };
		template.update(sql, param, keyHolder, keyColumnNames);
		order.setId(keyHolder.getKey().intValue());
		return order;
	}

	/**
	 * 注文が完了した商品を全て検索
	 * 
	 * @param userId 検索したいユーザー
	 * @return 検索した結果
	 */
	public List<Order> findByUserIdButAfterOrder(int userId) {
		String sql = "SELECT id,user_id,status,total_price,order_date,destination_name,destination_email,destination_zipcode,destination_address,destination_tel,delivery_time,payment_method FROM orders where user_id = :userId AND status=1 OR status=2 OR status=3 OR status=4 OR status=9;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		List<Order> orderList = template.query(sql, param, ORDER_ROW_MAPPER);
		if (orderList.size() == 0) {
			return null;
		}
		return orderList;
	}

	/**
	 * Ordersテーブルを主キー検索する.
	 * 
	 * @param id 注文ID
	 * @return 取得された注文情報
	 */
	public Order load(Integer id) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT orders.id as orderId, orders.user_id as orderUserId, orders.status as orderStatus, orders.total_price as orderTotalPrice, orders.order_date as orderOrderDate, orders.destination_name as orderDestinationName, orders.destination_email as orderDestinationEmail, orders.destination_zipcode as orderDestinationZipcode, orders.destination_address as orderDestinationAddress, orders.destination_tel as orderDestinationTel, orders.delivery_time as orderDeliveryTime, orders.payment_method as orderPaymentMethod, ");
		sql.append(
				"orderItems.id as orderItemId, orderItems.item_id as orderItemItemId, orderItems.order_id as orderItemOrderId, orderItems.quantity as orderItemQuantity, orderItems.size as orderItemSize, ");
		sql.append(
				"orderToppings.id as orderToppingId, orderToppings.topping_id as orderToppingToppingId, orderToppings.order_item_id as orderToppingOrderItemId, ");
		sql.append(
				"items.id as itemId, items.name as itemName, items.description as itemDescription, items.price_m as itemPriceM, items.price_l as itemPriceL, items.image_path as itemImagePath, items.deleted as itemDeleted, ");
		sql.append(
				"toppings.id as toppingId, toppings.name as toppingName, toppings.price_m as toppingPriceM, toppings.price_l as toppingPriceL ");
		sql.append("FROM orders as " + ORDERTABLE + " ");
		sql.append("LEFT OUTER JOIN " + ORDERITEMTABLE + " as orderItems ON orders.id = orderItems.order_id ");
		sql.append("LEFT OUTER JOIN " + ORDERTOPPINGTABLE
				+ " as orderToppings ON orderItems.id = orderToppings.order_item_id ");
		sql.append("LEFT OUTER JOIN " + ITEMTABLE + " as items ON orderItems.item_id = items.id ");
		sql.append("LEFT OUTER JOIN " + TOPPINGTABLE + " as toppings ON orderToppings.topping_id = toppings.id ");
		sql.append("WHERE orders.id=:id ORDER BY orderItems.id;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Order order = template.query(sql.toString(), param, RSE_ORDER);
		return order;
	}

	/**
	 * Ordersテーブルを更新する.
	 * 
	 * @param order 更新したい注文情報
	 */
	public void update(Order order) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(order);
		String updateSql = "UPDATE orders SET user_id=:userId,status=:status,total_price=:totalPrice,order_date=:orderDate,destination_name=:destinationName"
				+ ",destination_email=:destinationEmail,destination_zipcode=:destinationZipcode,destination_address=:destinationAddress"
				+ ",destination_tel=:destinationTel,delivery_time=:deliveryTime,payment_method=:paymentMethod WHERE id=:id;";
		template.update(updateSql, param);
	}
	
	
	/**
	 * オーダーを削除する.
	 * 
	 * @param id 削除するオーダー
	 */
	public void delete(Integer id) {
		String sql = "DELETE FROM orders WHERE id=:id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);
	}
}
