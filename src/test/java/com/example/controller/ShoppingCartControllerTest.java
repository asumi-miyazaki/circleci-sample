package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
import com.example.domain.Coupon;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

@SpringBootTest
@DbUnitConfiguration(dataSetLoader = XlsDataSetLoader.class)
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    TransactionDbUnitTestExecutionListener.class
})
class ShoppingCartControllerTest {

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

	/**
	 * 表示系のサンプルその２（アサーションの部分は未完成）
	 * 
	 * テスト実行前に初期データの投入がある例（本テスト実施のために必要な個別のテストデータ。data.sqlとは別）
	 * またテスト対象コントローラー内でセッションからuserを取得するので、そのセットもおこなっている例
	 */
	@Test
	@DisplayName("ショッピングカートの表示テスト（クーポンありユーザ）")
	@DatabaseSetup("classpath:ShoppingCartController/setup_01.xlsx") // テスト実行前に初期データを投入
	void testShowItemShoppingCart_01() throws Exception {

		MvcResult mvcResult = mockMvc.perform(get("/shopping-cart")
				.session(mockSession))	// 上で作成したUserをセッションに入れる（セッションのユーザのクーポンが取得されるため）
				.andExpect(view().name("cart_list"))
				.andExpect(model().attributeExists("coupon"))
				.andExpect(model().attributeExists("order"))
				.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();
		Coupon coupon = (Coupon) mav.getModel().get("coupon");
		System.out.println(coupon);
		// TODO: アサーション
		assertEquals(1,1);

	}
	
	/**
	 * 登録系のサンプルその２
	 * 処理後変更されるテーブルが複数ある場合
	 * 
	 * 画面から実行した場合、カート遷移後はordersテーブルのtotal_priceに金額が入っているが
	 * 以下を実行した時点ではまだtotal_priceは「0円」なので注意
	 * なんと一覧画面にリダイレクトした「showItemShoppingCart(Model)」の方で価格の更新処理をおこなっている
	 * 
	 * @throws Exception
	 */
	@Test
	@DisplayName("ショッピングカート追加のテスト（ユーザーログイン済み）")
	@ExpectedDatabase(value = "classpath:ShoppingCartController/insert_01.xlsx", assertionMode = DatabaseAssertionMode.NON_STRICT)
	void testAddShoppingCart_01() throws Exception {
		mockMvc.perform(multipart("/shopping-cart/insert")
				.session(mockSession)
                .param("itemId", "15")
                .param("quantity", "2")
                .param("size", "M")
                .param("toppingList", "1,2")
                .with(req -> {
                    req.setMethod(HttpMethod.POST.name());
                    return req;
                }));
	}
	


}
