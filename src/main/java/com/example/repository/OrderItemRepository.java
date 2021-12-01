package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.domain.Item;
import com.example.domain.OrderItem;

/**
 * order_itemsテーブルを操作するRepositoryクラス
 * 
 * @author inada
 *
 */
@Repository
public class OrderItemRepository {

	private static final RowMapper<OrderItem> ORDER_ITEM_ROWMAPPER = (rs, i) -> {
		OrderItem orderItem = new OrderItem();
		Item item = new Item();

		orderItem.setItemId(rs.getInt("ItemId"));
		orderItem.setCount(rs.getInt("count"));

		item.setId(rs.getInt("id"));
		item.setName(rs.getString("name"));
		item.setDescription(rs.getString("description"));
		item.setImagePath(rs.getString("imagePath"));
		orderItem.setItem(item);

		return orderItem;
	};

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * 注文商品をショッピングカートに追加する.
	 * 
	 * @param orderItem 追加したい商品
	 * @return 追加した商品
	 */
	public OrderItem insert(OrderItem orderItem) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderItem);
		String sql = "INSERT INTO order_items (item_id,order_id,quantity,size) VALUES (:itemId,:orderId,:quantity,:size);";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnName = { "id" };
		template.update(sql, param, keyHolder, keyColumnName);
		orderItem.setId(keyHolder.getKey().intValue());
		return orderItem;
	}

	/**
	 * 注文商品と注文商品のトッピングを削除する.
	 * 
	 * @param orderItemId 削除したい注文商品
	 */
	public void deleteOrderItemWithOrderTopping(int orderItemId) {
		String sql = "WITH deleted AS (DELETE FROM order_items WHERE id = :id RETURNING id) DELETE FROM order_toppings WHERE order_item_id IN (SELECT id FROM deleted);";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", orderItemId);
		template.update(sql, param);
	}

	/**
	 * 仮IDの注文をUserIDの注文に変更.
	 * 
	 * @param orderItem 変更したい注文
	 */
	public void update(OrderItem orderItem) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderItem);
		String sql = "UPDATE order_items SET item_id=:itemId,order_id=:orderId,quantity=:quantity,size=:size WHERE id=:id;";
		template.update(sql, param);
	}
	
	
	/**
	 * 注文回数降順に注文商品情報を取得する.
	 * 
	 * @return 取得された注文商品のリスト
	 */
	public List<OrderItem> count() {
		String sql = "SELECT order_items.item_id as itemId,COUNT(order_items.item_id) as count ,items.id as id, items.name as name , items.description as description ,\r\n"
				+ "items.image_path as imagePath FROM order_items LEFT OUTER JOIN items ON order_items.item_id = items.id GROUP BY itemId,items.id,name,description,imagePath ORDER BY count(item_id) DESC;";
		List<OrderItem> orderItemList = template.query(sql, ORDER_ITEM_ROWMAPPER);
		return orderItemList;
	}
}
