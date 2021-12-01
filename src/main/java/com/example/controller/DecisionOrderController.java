package com.example.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.Coupon;
import com.example.domain.Order;
import com.example.domain.User;
import com.example.form.DecisionOrderForm;
import com.example.service.CouponService;
import com.example.service.DecisionOrderService;

/**
 * 宛先や支払方法を操作するコントローラークラス.
 * 
 * @author izawamotoki
 *
 */
@Controller
@RequestMapping("/decisionorder")
@EnableAsync
public class DecisionOrderController {

	@Autowired
	private DecisionOrderService decisionOrderService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private HttpSession session;

	@ModelAttribute
	public DecisionOrderForm setUpDecisionOrderForm() {
		return new DecisionOrderForm();
	}
	
	/**
	 * 注文確認画面に遷移する.
	 * 
	 * @return 注文確認画面
	 */
	@RequestMapping("/toConfirmOrder")
	public String toConfirmOrder() {
		return "order_confirm";
	}

	/**
	 * 注文完了画面に遷移する.
	 * 
	 * @return 注文完了画面
	 */
	@RequestMapping("/toOrderFinished")
	public String toOrderFinished() {
		return "order_finished";
	}

	/**
	 * 宛先や支払方法等の情報の登録を行う.
	 * 
	 * @param form    宛先や支払方法等の情報用フォーム.
	 * @param result  エラー情報
	 * @param orderId 入力された注文情報ID
	 * @param model   リクエストスコープ
	 * @return 注文確認画面
	 * @return 注文完了画面
	 */
	@RequestMapping("")
	public String DecisionOrder(@Validated DecisionOrderForm form, BindingResult result, Integer orderId, Model model, RedirectAttributes redirectAttributes) {
		User user = (User) session.getAttribute("user");
		Coupon coupon = couponService.searchByUserHaveCoupon(user.getId());
		if(coupon.getId() != null) {
			couponService.updateCoupon(coupon, orderId);
		}
		
		if (result.hasErrors()) {
			Order order = decisionOrderService.load(orderId);
			model.addAttribute("order", order);
			model.addAttribute("coupon",coupon);
			return toConfirmOrder();
		}

		String deliveryTime = form.getDeliveryTimeDate().replace("-", "/") + " " + form.getDeliveryTimeHour();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Timestamp deliveryTimeStamp;
		try {
			deliveryTimeStamp = new Timestamp(simpleDateFormat.parse(deliveryTime).getTime());
			form.setDeliveryTime(deliveryTimeStamp);
		} catch (ParseException e) {
			Order order = decisionOrderService.load(orderId);
			model.addAttribute("order", order);
			return "order_confirm";
		}

		long millis = System.currentTimeMillis();
		long deliveryTimeFor3Hourago = 0;
		deliveryTimeFor3Hourago = form.getDeliveryTime().getTime();
		long difference = deliveryTimeFor3Hourago - millis;

		if (difference < 10800000) {
			Order order = decisionOrderService.load(orderId);
			model.addAttribute("order", order);
			model.addAttribute("coupon",coupon);
			result.rejectValue("deliveryTime", "", "今から3時間後の日時をご入力ください");
			return "order_confirm";
		}

		Calendar calendar = Calendar.getInstance();
		form.setOrderDate(calendar.getTime());
		decisionOrderService.DecisionOrder(form, coupon);
		decisionOrderService.sendMail(form);

		coupon = couponService.insertCoupon(user.getId());
		redirectAttributes.addFlashAttribute("coupon", coupon);
		
		return "redirect:/decisionorder/toOrderFinished";
	}

}
