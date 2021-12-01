package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Comment;
import com.example.domain.Item;
import com.example.domain.Topping;
import com.example.repository.CommentRepository;
import com.example.repository.ItemRepository;
import com.example.repository.ToppingRepository;

/**
 * 商品詳細情報を操作するサービスクラス.
 * 
 * @author kojiro0706
 *
 */
@Service
public class ShowItemDetailService {
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ToppingRepository toppingRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	/**
	 * 商品詳細情報を取得する.
	 * 
	 * @param id
	 * @return　商品情報
	 */
	public Item showItemDetail(Integer id) {
		Item item = itemRepository.load(id);
		List<Topping> toppingList = toppingRepository.findAll();
		item.setToppingList(toppingList);
		List<Comment> commentList = commentRepository.findByItemId(id);
		item.setCommentList(commentList);
		return item;
	}
	
	

}
