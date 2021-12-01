package com.example.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ログイン確認を行い際の業務処理をするServiceクラス.
 * 
 * @author inada
 *
 */
@Service
public class LoginCheckService {

	@Autowired
	private HttpSession session;

	/**
	 * セッションスコープにuserが格納されていなければログイン画面に遷移する.
	 * 
	 * @param url ログイン後に戻ってくるURL
	 * @return 真偽値
	 */
	public Boolean loginCheck(String url) {
		if (session.getAttribute("user") == null) {
			session.setAttribute("url", url);
			return true;
		}
		session.removeAttribute("url");
		session.removeAttribute("temporalUserId");
		return false;
	}
}
