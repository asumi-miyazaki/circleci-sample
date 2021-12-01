package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.example.dataset.TestUtil;
import com.example.dataset.XlsDataSetLoader;
import com.example.domain.Comment;
import com.example.domain.Item;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

@SpringBootTest
@DbUnitConfiguration(dataSetLoader = XlsDataSetLoader.class)
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    TransactionDbUnitTestExecutionListener.class
})

class ShowItemDetailControllerTest {
	
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	private MockHttpSession mockSession;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		mockSession = TestUtil.createMockUserSession();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DatabaseSetup("classpath:ShowItemDetailController/showdetailcontroller.xlsx") // テスト実行前に初期データを投入
	void testShowItemDetail() throws Exception {
		mockSession = TestUtil.createMockItemSession();
		MvcResult mvcResult = mockMvc.perform(get("/show-item-detail?id=15").session(mockSession))
				.andExpect(view().name("item_detail"))
				.andReturn();
		
		//requestスコープにid=15のitemの情報が入っているか（トッピングリストは個数だけ、コメントリストは最初と最後だけ）
		ModelAndView mav = mvcResult.getModelAndView();
		Item actItem = (Item) mav.getModel().get("item");
		List<Comment> actCommentList = actItem .getCommentList();
				
		
		Item expItem = new Item();
		expItem.setId(15);
		expItem.setName("旨辛味噌麺");
		expItem.setDescription("味噌ラーメンの常識を変える一杯。濃厚かつコクと深みのあるスープ、小麦の味がこみ上げる極太麺、ジューシーなチャーシューが胃袋を鷲掴みにする。");
		expItem.setPriceM(680);
		expItem.setPriceL(800);
		expItem.setImagePath("15.jpg");
		expItem.setDeleted(false);
		
		//トッピングリストの期待値を個数として考える
		int expToppingListSize = 18;
		
		Comment expFirstComment = new Comment();
		expFirstComment.setId(6);
		expFirstComment.setName("ラーメン六郎");
		expFirstComment.setContent("ここのラーメン美味すぎる");
		expFirstComment.setItemId(15);
		
		Comment expLastComment = new Comment();
		expLastComment.setId(1);
		expLastComment.setName("ラーメン太郎");
		expLastComment.setContent("ここのラーメン美味すぎる");
		expLastComment.setItemId(15);
		
		Comment actFirstComment = actCommentList.get(0);
		Comment actLastComment = actCommentList.get(actCommentList.size()-1);
		
		assertEquals(expItem.getId(),actItem.getId());
		assertEquals(expItem.getName(),actItem.getName());
		assertEquals(expItem.getDescription(), actItem.getDescription());
		assertEquals(expItem.getPriceM(), actItem.getPriceM());
		assertEquals(expItem.getPriceL(), actItem.getPriceL());
		assertEquals(expItem.getImagePath(), actItem.getImagePath());
		assertEquals(expItem.getDeleted(),actItem.getDeleted());
		
		assertEquals(expToppingListSize, actItem.getToppingList().size());
		
		assertEquals(expFirstComment.getId(), actFirstComment.getId());
		assertEquals(expFirstComment.getName(), actFirstComment.getName());
		assertEquals(expFirstComment.getContent(), actFirstComment.getContent());
		assertEquals(expFirstComment.getItemId(), actFirstComment.getItemId());
		
		assertEquals(expLastComment.getId(), actLastComment.getId());
		assertEquals(expLastComment.getName(), actLastComment.getName());
		assertEquals(expLastComment.getContent(), actLastComment.getContent());
		assertEquals(expLastComment.getItemId(), actLastComment.getItemId());
		
	}

}
