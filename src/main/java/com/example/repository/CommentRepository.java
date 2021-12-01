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

import com.example.domain.Comment;

/**
 * Commentsテーブルを操作するリポジトリ.
 * 
 * @author kojiro0706
 *
 */
@Repository
public class CommentRepository {

	@Autowired

	private NamedParameterJdbcTemplate template;

	private static final RowMapper<Comment> COMMENT_ROW_MAPPER = new BeanPropertyRowMapper<>(Comment.class);

	/**
	 * 商品に紐付いたコメントを検索する.
	 * 
	 * @param itemId 商品ID
	 * @return コメント情報
	 */
	public List<Comment> findByItemId(Integer itemId) {

		String sql = "SELECT id, name, content,item_id FROM comments" + " WHERE item_id = :itemId ORDER BY id DESC;";

		SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", itemId);

		return template.query(sql, param, COMMENT_ROW_MAPPER);
	}

	/**
	 * コメント情報を挿入する.
	 * 
	 * @param comment コメント情報
	 */
	public void insert(Comment comment) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(comment);
		String sql = "INSERT INTO comments(name, content, item_id)VALUES(:name,:content,:itemId)";
		template.update(sql, param);
	}

	/**
	 * コメントを削除する.
	 * 
	 * @param id コメントID
	 */
	public void deleteByCommentId(Integer id) {
		String sql = "DELETE FROM comments WHERE id = :id";

		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

		template.update(sql, param);
	}

}
