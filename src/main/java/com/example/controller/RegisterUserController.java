package com.example.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.User;
import com.example.form.RegisterUserForm;
import com.example.service.RegisterUserService;

/**
 * ユーザー情報を操作するコントローラークラス.
 * 
 * @author kojiro0706
 *
 */
@Controller
@RequestMapping("/registerUser")
public class RegisterUserController {

	@Autowired
	private RegisterUserService registerUserService;

	/**
	 * ユーザー情報登録画面を出力する.
	 * 
	 * @return ユーザー情報登録画面
	 */
	@RequestMapping("/toInsert")
	public String toInsert() {
		return "register_user";
	}

	@ModelAttribute
	public RegisterUserForm setUpRegisterUserForm() {
		return new RegisterUserForm();
	}

	/**
	 * ユーザー情報の登録を行う.
	 * 
	 * @param form ユーザー情報用フォーム.
	 * @return ログイン画面
	 */
	@RequestMapping("/insert")
	public String insert(@Validated RegisterUserForm form, BindingResult result) {

		if (!(form.getPassword().equals(form.getConfirmationPassword()))) {
			result.rejectValue("password", "", "パスワードと確認用パスワードが不一致です");
			result.rejectValue("confirmationPassword", "", "");
		}

		User existUser = registerUserService.findByEmail(form.getEmail());
		if (existUser != null) {
			result.rejectValue("email", "", "そのメールアドレスはすでに使われています");
		}

		if (result.hasErrors()) {
			return toInsert();
		}

		User user = new User();
		BeanUtils.copyProperties(form, user);
		registerUserService.insert(user);

		return "redirect:/login/toLogin";
	}
}
