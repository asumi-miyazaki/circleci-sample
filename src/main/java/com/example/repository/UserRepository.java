package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.User;

/**
 * userテーブルを操作するリポジトリクラス.
 * 
 * @author kojiro0706
 *
 */
@Repository
public class UserRepository {
	
	/**
	 * Userオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<User> USER_ROW_MAPPER =
			new BeanPropertyRowMapper<>(User.class);

	@Autowired
	private NamedParameterJdbcTemplate template;
	
	/**
	 * ユーザー情報を挿入する.
	 * 
	 * @param user ユーザー情報
	 */
	public void insert(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = "INSERT INTO users(name,email,password,zipcode,address,telephone)"
				+ "VALUES(:name,:email,:password,:zipcode,:address,:telephone)";
		template.update(sql, param);
	}
	
	/**
	 * メールアドレスからユーザー情報を取得する.
	 * 
	 * @param email　メールアドレス
	 * @return ユーザー情報
	 */
	public User findByEmail(String email) {
		String sql = "SELECT id,name,email,password,zipcode,address,telephone FROM"
				+ " users WHERE email=:email";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
		List<User> userList = template.query(sql, param,USER_ROW_MAPPER);
		if(userList.size() == 0) {
			return null;
		}else {
			return userList.get(0);
		}
	}
	
	/**
	 * メールアドレスとパスワードからユーザー情報を取得する.
	 * 
	 * @param email メールアドレス
	 * @param password　パスワード
	 * @return ユーザー情報
	 */
	public User findByEmailAndPassword(String email,String password) {
		String sql = "SELECT id,name,email,password,zipcode,address,telephone FROM"
				+ " users WHERE email=:email AND password=:password";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email).addValue("password",
				password);
		List<User> userList = template.query(sql, param,USER_ROW_MAPPER);
		if(userList.size() == 0) {
		return null;
		}
		return userList.get(0);
	}
}
