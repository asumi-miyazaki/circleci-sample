package com.example.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.service.DeleteCommentService;
import com.example.service.ShowItemDetailService;

@Controller
@RequestMapping("/delete-comment")
public class DeleteCommentController {

	@Autowired
	private DeleteCommentService deleteCommentService;

	@Autowired
	private ShowItemDetailService showItemDetailService;

	@Autowired
	private HttpSession session;

	/**
	 * コメントを削除する.
	 * 
	 * @param commentId コメントID
	 * @param itemId    商品ID
	 * @return 商品詳細画面
	 */
	@RequestMapping("")
	public String deleteComment(Integer commentId, Integer itemId) {

		deleteCommentService.deleteByCommentId(commentId);// コメントを消し、消す前と比べてどうなっているか、を比べている

		Item item = showItemDetailService.showItemDetail(itemId);
		session.setAttribute("item", item);

		return "redirect:/show-item-detail";
	}

}
