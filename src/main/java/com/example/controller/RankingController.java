package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.OrderItem;
import com.example.service.RankingService;

@Controller
@RequestMapping("/ranking")
public class RankingController {

	@Autowired
	private RankingService rankingService;
	

	/**
	 * ランキングページを表示する.
	 * 
	 * @param model リクエストスコープ
	 * @return ランキングページ
	 */
	@RequestMapping("")
	public String ranking(Model model) {
		List<OrderItem> orderItemList = rankingService.ranking();
		model.addAttribute("orderItemList", orderItemList);
		for (OrderItem a : orderItemList) {
			System.out.println(a);
		}
		return "ranking";
	}

}
