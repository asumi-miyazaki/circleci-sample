package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.example.dataset.XlsDataSetLoader;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

@SpringBootTest
@DbUnitConfiguration(dataSetLoader = XlsDataSetLoader.class)
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class, // このテストクラスでDIを使えるように指定
    TransactionDbUnitTestExecutionListener.class // @DatabaseSetupや@ExpectedDatabaseなどを使えるように指定
})
class RegisterUserControllerTest {

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
	 * 登録系のサンプルその１
	 * 
	 * 登録、更新系は、実行後のテーブルの状態（期待値）をExcelで作成しておき、DBUnitの機能でチェックをおこなう（@ExpectedDatabaseの所）　
	 * ※基本テーブル名はExcelのシート名で合わせればいいはずだが、usersテーブルは何故かそれだとエラーが出てしまい
	 * 「table = "users"」を入れることで回避できた。他のテーブルのチェックの際はおそらくいらない（参照「testAddShoppingCart_01()」）
	 */
	@Test
	@DisplayName("ユーザー登録テスト（正常系）")
	@ExpectedDatabase(value = "classpath:RegisterUserController/insert_01.xlsx", table = "users", assertionMode = DatabaseAssertionMode.NON_STRICT)
	void testInsert() throws Exception {
		
		mockMvc.perform(multipart("/registerUser/insert")
                .param("name", "ナナシ")
                .param("email", "nanashi@example.com")
                .param("password", "12345678")
                .param("confirmationPassword", "12345678")
                .param("zipcode", "111-1111")
                .param("address", "東京都新宿区")
                .param("telephone", "080-0000-0000")
//                .file(new MockMultipartFile("portrait", "some file name", "image/png", Files.readAllBytes(Paths.get("some path")))
                .with(req -> {
                    req.setMethod(HttpMethod.POST.name());
                    return req;
                }));
	}
	
	/**
	 * コントローラでの入力値チェックでのエラーを検証する例
	 */
	@Test
	@DisplayName("ユーザー登録テスト（メールアドレス重複でエラー）")
	void testInsertDuplicate() throws Exception {
		
		MvcResult mvcResult = mockMvc.perform(multipart("/registerUser/insert")
                .param("name", "ナナシ")
                .param("email", "test@test.co.jp")
                .param("password", "12345678")
                .param("confirmationPassword", "12345678")
                .param("zipcode", "111-1111")
                .param("address", "東京都新宿区")
                .param("telephone", "080-0000-0000")
                .with(req -> {
                    req.setMethod(HttpMethod.POST.name());
                    return req;
                }))
				.andExpect(view().name("register_user"))
				.andReturn();

		// エラーメッセージをチェック
		ModelAndView mav = mvcResult.getModelAndView();
        BindingResult result = (BindingResult) mav.getModel().get(BindingResult.MODEL_KEY_PREFIX + "registerUserForm");
        assertEquals("そのメールアドレスはすでに使われています", result.getFieldError().getDefaultMessage());
		
	}
	

}
