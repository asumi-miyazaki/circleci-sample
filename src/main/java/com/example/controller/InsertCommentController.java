package com.example.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Comment;
import com.example.domain.Item;
import com.example.form.InsertCommentForm;
import com.example.service.InsertCommentService;
import com.example.service.ShowItemDetailService;

/**
 * コメント情報を登録するコントローラークラス.
 * 
 * @author kojiro0706
 *
 */
@Controller
@RequestMapping("/insert-comment")
public class InsertCommentController {
	
	@Autowired
	private InsertCommentService insertCommentService;
	
	@Autowired
	private ShowItemDetailService showItemDetailService;
	
	@Autowired
	private HttpSession session;

	
	/**
	 * コメント情報を登録する.
	 * 
	 * @param form
	 * @return コメント情報
	 */
	@RequestMapping("")
	public String insertComment(InsertCommentForm form,Model model) {
		Comment comment = new Comment();
		BeanUtils.copyProperties(form, comment);
		comment.setItemId(Integer.parseInt(form.getItemId()));
		insertCommentService.insertComment(comment);
		
		Item item = showItemDetailService.showItemDetail(Integer.parseInt(form.getItemId()));
		session.setAttribute("item",item);
		
		return "redirect:show-item-detail";
		
	
	}

}
