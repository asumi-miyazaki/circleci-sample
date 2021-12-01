package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.domain.Item;
import com.example.repository.CommentRepository;
import com.example.repository.ItemRepository;

/**
 * 商品情報を操作するサービスクラス.
 * 
 * @author kojiro0706
 *
 */
@Service
public class ShowListService {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	CommentRepository commentRepository;

	/**
	 * 商品情報を取得する.
	 * 
	 * @param name  曖昧検索する文字列
	 * @param sort  並べ替えを行う文字列
	 * @param model 表示に使用
	 * @return 商品情報
	 */
	public List<Item> showList(String name, String sortDisplay, Model model) {
		String sort = null;
		if (sortDisplay == null || sortDisplay.equals("row")) {
			sort = "asc";
		} else {
			sort = "desc";
		}
		model.addAttribute("sort", sort);
		if (name == null) {
			List<Item> itemList = itemRepository.findAll(sort);
			return itemList;
		} else {
			List<Item> itemList = itemRepository.findByName(name, sort);
			if (itemList.size() == 0) {
				model.addAttribute("emptyMessage", "該当する商品がありません");
				itemList = itemRepository.findAll(sort);
			}
			return itemList;
		}
	}

}
