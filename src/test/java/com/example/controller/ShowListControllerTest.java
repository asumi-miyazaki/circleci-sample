package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.example.domain.Item;

@SpringBootTest
class ShowListControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * 表示系のサンプルその１（アサーションの部分は未完成）
	 * 
	 * ①コントローラー呼出し
	 * ②スコープのデータを取り出し
	 * ③assertEqualsでチェック
	 */
	@Test
	@DisplayName("商品一覧画面表示（絞り込みなし、並び順もデフォルト）")
	void testShowList_01() throws Exception {
		// ①コントローラー呼出し
		MvcResult mvcResult = mockMvc.perform(get("/"))
				.andExpect(view().name("item_list_noodle"))
				.andReturn();

		// ②スコープのデータを取り出し
	    ModelAndView mav = mvcResult.getModelAndView();
	    @SuppressWarnings(value = "unchecked")// 下のキャストのワーニングを出さないようにする
        List<List<Item>> itemListRow = ((List<List<Item>>) mav.getModel().get("itemListRow"));
	    System.out.println(itemListRow);

	    // TODO: ③assertEqualsでチェック
	    assertEquals(1,1);
	}
	
}
