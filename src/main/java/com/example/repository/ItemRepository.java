package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Item;

/**
 * itemテーブルを操作するリポジトリクラス.
 * 
 * @author kojiro0706
 *
 */
@Repository
public class ItemRepository {

	/**
	 * Itemオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<Item> ITEM_ROW_MAPPER = new BeanPropertyRowMapper<>(Item.class);

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * 商品情報を全件取得する.
	 * 
	 * @return 商品情報
	 */
	public List<Item> findAll(String sort) {
		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted" + " FROM items ORDER BY price_m "
				+ sort + " ;";

		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);

		return itemList;
	}

	/**
	 * 主キーから商品情報を取得する.
	 * 
	 * @param id 商品ID
	 * @return 商品情報
	 */
	public Item load(Integer id) {
		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted"
				+ " FROM items WHERE id=:id ORDER BY price_m ";

		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

		Item item = template.queryForObject(sql, param, ITEM_ROW_MAPPER);

		return item;
	}

	/**
	 * 名前から曖昧検索をする.
	 * 
	 * @param name 商品名
	 * @return 検索された商品名
	 */
	public List<Item> findByName(String name, String sort) {

		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted"
				+ " FROM items WHERE name ILIKE :name ORDER BY price_m " + sort + " ;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
		return itemList;
	}

}
