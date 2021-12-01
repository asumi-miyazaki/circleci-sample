package com.example.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ユーザー情報を操作するコントローラークラス.
 * 
 * @author kojiro0706
 *
 */
@Controller
@RequestMapping("/logout")
public class LogoutController {

	@Autowired
	private HttpSession session;

	/**
	 * ログアウトする.
	 * 
	 * @return 商品一覧画面
	 */
	@RequestMapping("/")
	public String logout() {
		session.invalidate();
		System.out.println();
		return "redirect:/";
	}

}
