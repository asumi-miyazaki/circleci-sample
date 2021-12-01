package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.domain.Coupon;

/**
 * couponテーブルを操作するRepositoryクラス.
 * 
 * @author inada
 *
 */
@Repository
public class CouponRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<Coupon> COUPON_ROW_MAPPER = new BeanPropertyRowMapper<>(Coupon.class);

	/**
	 * ユーザーが保持しているクーポンを取得.
	 * 
	 * @param userId ログインしているユーザ
	 * @return 取得した情報
	 */
	public Coupon findByUserId(Integer userId) {
		String sql = "SELECT id,name,code,discount,user_id,order_id,deleted FROM coupons WHERE user_id = :userId AND deleted = false;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		List<Coupon> couponList = template.query(sql, param, COUPON_ROW_MAPPER);
		if (couponList.size() == 0) {
			return null;
		}
		return couponList.get(0);
	}

	/**
	 * オーダーで使用したクーポンを取得.
	 * 
	 * @param orderId オーダーID
	 * @return 取得したクーポン情報
	 */
	public Coupon findByOrderId(Integer orderId) {
		String sql = "SELECT id,name,code,discount,user_id,order_id,deleted FROM coupons WHERE order_id = :orderId AND deleted = true;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("orderId", orderId);
		List<Coupon> couponList = template.query(sql, param, COUPON_ROW_MAPPER);
		if (couponList.size() == 0) {
			return null;
		}
		return couponList.get(0);
	}

	/**
	 * クーポンを主キー検索する.
	 * 
	 * @param id 検索するID
	 * @return 検索したクーポン
	 */
	public Coupon load(Integer id) {
		String sql = "SELECT id,name,code,discount,user_id,order_id,deleted FROM coupons WHERE id = :id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Coupon coupon = template.queryForObject(sql, param, COUPON_ROW_MAPPER);
		return coupon;
	}

	/**
	 * クーポンを登録する.
	 * 
	 * @param coupon 登録するクーポン
	 * @return 登録したクーポンID
	 */
	public Integer insert(Coupon coupon) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(coupon);
		String sql = "INSERT INTO coupons (name,code,discount,user_id,order_id) VALUES (:name,:code,:discount,:userId,:orderId);";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnName = { "id" };
		template.update(sql, param, keyHolder, keyColumnName);
		Integer id = keyHolder.getKey().intValue();
		return id;
	}

	/**
	 * クーポンを削除する.
	 * 
	 * @param id 削除するクーポンID
	 */
	public void update(Coupon coupon) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(coupon);
		String sql = "UPDATE coupons SET name=:name,code=:code,discount=:discount,user_id=:userId,order_id=:orderId,deleted=:deleted WHERE id = :id;";
		template.update(sql, param);
	}
}
