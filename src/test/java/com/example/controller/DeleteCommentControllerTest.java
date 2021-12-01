package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.dataset.TestUtil;
import com.example.dataset.XlsDataSetLoader;
import com.example.domain.Comment;
import com.example.domain.Item;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

@SpringBootTest
@DbUnitConfiguration(dataSetLoader = XlsDataSetLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, // このテストクラスでDIを使えるように指定
		TransactionDbUnitTestExecutionListener.class // @DatabaseSetupや@ExpectedDatabaseなどを使えるように指定
})
class DeleteCommentControllerTest {
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
	@DatabaseSetup("classpath:CommentController/Comment.xlsx")
	@ExpectedDatabase(value = "classpath:CommentController/DeleteComment.xlsx", table = "comments", assertionMode = DatabaseAssertionMode.NON_STRICT)
	void testDeleteComment() throws Exception {
		mockMvc.perform(multipart("/delete-comment")
				.param("commentId", "6")
				.param("itemId", "1")

				.with(req -> {
					req.setMethod(HttpMethod.POST.name());
					return req;
				})).andExpect(view().name("redirect:/show-item-detail"));

	}
	
	
	@DisplayName("セッションスコープにItemの情報(コメントリスト)が入っている")
	@Test
	@DatabaseSetup("classpath:CommentController/Comment.xlsx")
	void insertCommentTest3() throws Exception {
		MvcResult mvcResult = mockMvc.perform(multipart("/delete-comment")
				.session(mockSession)//擬似的にログインするために必要
				.param("commentId", "7")
				.param("itemId", "1")
				.with(req -> {
					req.setMethod(HttpMethod.POST.name());
					return req;
				})).andReturn();
		Item actItem = (Item) mvcResult.getRequest().getSession().getAttribute("item");
		List<Comment> actCommentList = actItem .getCommentList();
		actCommentList.forEach(System.out::println);
		
		//一番上のコメントの情報をsetする
		Comment expFirstComment = new Comment();
		expFirstComment.setId(6);
		expFirstComment.setName("ラーメン六郎");
		expFirstComment.setContent("ここのラーメン美味すぎる");
		expFirstComment.setItemId(1);
		
		//一番下の情報をsetする
		Comment expLastComment = new Comment();
		expLastComment.setId(1);
		expLastComment.setName("ラーメン太郎");
		expLastComment.setContent("ここのラーメン美味すぎる");
		expLastComment.setItemId(1);
		
		//newしないで作る,
		Comment actFirstComment = actCommentList.get(0);
		Comment actLastComment = actCommentList.get(actCommentList.size()-1);
		
		//ここは変わらない
		assertEquals(expFirstComment.toString(), actFirstComment.toString());
		assertEquals(expLastComment.toString(), actLastComment.toString());
	}		
}
