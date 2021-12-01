package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.Coupon;
import com.example.domain.Order;
import com.example.domain.User;
import com.example.service.CouponService;
import com.example.service.OrderHistoryService;

/**
 * 注文履歴表示を操作するControllerクラス.
 * 
 * @author inada
 *
 */
@Controller
@RequestMapping("/order-history")
public class OrderHistoryController {

	@Autowired
	private OrderHistoryService orderHistoryService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private HttpSession session;

	/**
	 * 注文履歴を表示する.
	 * 
	 * @param model 注文履歴を表示するために使用
	 * @return 検索した注文履歴
	 */
	@RequestMapping("")
	public String index(Model model) {
		User user = (User) session.getAttribute("user");
		List<Order> orderList = orderHistoryService.SearchAfterOrderUserId(user.getId());
		model.addAttribute("orderList", orderList);
		return "order_history";
	}

	/**
	 * 注文履歴の詳細を検索する
	 * 
	 * @param id                 検索する注文ID
	 * @param redirectAttributes リダイレクト時に使用
	 * @param model              エラー時に使用
	 * @return 注文履歴詳細
	 */
	@RequestMapping("/item-history")
	public String itemHistory(int id, RedirectAttributes redirectAttributes, Model model) {
		User user = (User) session.getAttribute("user");
		Order order = orderHistoryService.searchOrderIdAndCheckUserId(user.getId(), id);
		if (order == null) {
			redirectAttributes.addFlashAttribute("error", "検索した注文が見つかりませんでした。");
			return "redirect:/order-history";
		}
		Coupon coupon = couponService.searchByOrderUseCoupon(order.getId());
		model.addAttribute("coupon", coupon);
		model.addAttribute("order", order);
		return "order_item_history";
	}
}
