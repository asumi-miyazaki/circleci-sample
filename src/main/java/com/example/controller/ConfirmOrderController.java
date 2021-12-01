package com.example.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Coupon;
import com.example.domain.Order;
import com.example.domain.User;
import com.example.form.DecisionOrderForm;
import com.example.service.ConfirmOrderService;
import com.example.service.CouponService;
import com.example.service.LoginCheckService;
import com.example.service.ShoppingCartService;

/**
 * 注文確認画面表示時に使用するControllerクラス.
 * 
 * @author izawa
 *
 */

@Controller
@RequestMapping("/confirm-order")
public class ConfirmOrderController {

	@Autowired
	private ConfirmOrderService confirmOrderService;

	@Autowired
	private LoginCheckService loginCheckService;

	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private CouponService couponService;

	@ModelAttribute
	public DecisionOrderForm setUpDecisionOrderForm() {
		return new DecisionOrderForm();
	}

	@Autowired
	private HttpSession session;

	/**
	 * 注文確認画面時に表示するショッピングカートの商品を取得する.
	 * 
	 * @param orderId ショッピングカートを取得したい注文のID
	 * @param userId  ショッピングカートを取得したい人のID
	 * @param model   リクエストスコープ
	 * @param request HTTP サーブレットのリクエスト情報
	 * @return 注文確認画面
	 */
	@RequestMapping("")
	public String confirmOrder(String orderId, String userId, Model model, HttpServletRequest request) {
		String url = request.getRequestURI();
		if (loginCheckService.loginCheck(url)) {
			return "redirect:/login/toLogin";
		}
		if (orderId == null) {
			User user = (User) session.getAttribute("user");
			Order order = shoppingCartService.showItemShoppingCart(user.getId());
			orderId = String.valueOf(order.getId());
		}
		Order order = confirmOrderService.confirmOrder(Integer.parseInt(orderId));
		order.setUser((User) session.getAttribute("user"));
		session.removeAttribute("orderId");
		Coupon coupon = couponService.searchByUserHaveCoupon(order.getUserId());
		model.addAttribute("coupon", coupon);
		model.addAttribute("order", order);
		return "order_confirm";
	}

}
