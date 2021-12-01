package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.CommentRepository;

/**
 * コメント情報を削除するサービスクラス.
 * 
 * @author kojiro0706
 *
 */
@Service
public class DeleteCommentService {

	
	@Autowired
	private CommentRepository commentRepository;
	
	/**
	 * コメント情報を削除する.
	 * 
	 * @param id コメントID
	 * 
	 */
	public void deleteByCommentId(Integer id) {
		
		commentRepository.deleteByCommentId(id);
	}
}
