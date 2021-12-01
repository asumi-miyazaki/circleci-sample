package com.example.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Coupon;
import com.example.domain.Order;
import com.example.domain.User;
import com.example.form.ShoppingCartForm;
import com.example.service.CouponService;
import com.example.service.ShoppingCartService;

/**
 * ショッピングカート表示時に使用するControllerクラス.
 * 
 * @author inada
 *
 */
@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private ShoppingCartForm setUpShoppingCartForm() {
		return new ShoppingCartForm();
	}

	@Autowired
	private HttpSession session;

	/**
	 * ショッピングカート一覧を表示する.
	 * 
	 * @param model ショッピングカートの商品を表示するために使用
	 * @return ショッピングカート一覧画面
	 */
	@RequestMapping("")
	public String showItemShoppingCart(Model model) {
		User user = null;
		if (session.getAttribute("user") != null) {
			user = (User) session.getAttribute("user");
		} else if (session.getAttribute("temporalUserId") != null) {
			user = (User) session.getAttribute("temporalUserId");
		} else {
			user = new User();
			user.setId(0);
		}
		Order order = shoppingCartService.showItemShoppingCart(user.getId());
		Coupon coupon = couponService.searchByUserHaveCoupon(user.getId());
		model.addAttribute("coupon", coupon);
		model.addAttribute("order", order);
		return "cart_list";
	}

	/**
	 * ショッピングカートに商品を追加する.
	 * 
	 * @param form   追加したい商品情報
	 * @param result エラー情報
	 * @return ショッピングカート一覧画面
	 */
	@RequestMapping("/insert")
	public String addItemShoppingCart(@Validated ShoppingCartForm form, BindingResult result) {
		if (result.hasErrors()) {
			return "item_detail";
		}
		User user = null;
		if (session.getAttribute("user") != null) {
			user = (User) session.getAttribute("user");
		} else if (session.getAttribute("temporalUserId") != null) {
			user = (User) session.getAttribute("temporalUserId");
		} else {
			user = new User();
			Random random = new Random();
			user.setId(random.nextInt(99999999) + 5000);
			session.setAttribute("temporalUserId", user);
		}
		shoppingCartService.addShoppingCart(form, user.getId());
		return "redirect:/shopping-cart";
	}

	/**
	 * ショッピングカートの商品を削除する.
	 * 
	 * @param orderItemId 削除したい商品ID
	 * @return ショッピングカート一覧画面
	 */
	@RequestMapping("/delete")
	public String deleteItemShoppingCart(int orderItemId) {
		shoppingCartService.deleteItemShoppingCart(orderItemId);
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "redirect:/shopping-cart";
	}
}
