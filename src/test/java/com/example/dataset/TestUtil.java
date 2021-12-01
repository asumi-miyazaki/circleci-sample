package com.example.dataset;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.mock.web.MockHttpSession;

import com.example.domain.Item;
import com.example.domain.User;

public class TestUtil {

	public static MockHttpSession createMockUserSession() {
		User user = new User();
		user.setId(1);
		user.setName("テストユーザ");
		Map<String, Object> sessionMap = new LinkedHashMap<String, Object>();
		sessionMap.put("user", user);
		return createMockHttpSession(sessionMap);
	}

	private static MockHttpSession createMockHttpSession(Map<String, Object> sessions) {
		MockHttpSession mockHttpSession = new MockHttpSession();
		for (Map.Entry<String, Object> session : sessions.entrySet()) {
			mockHttpSession.setAttribute(session.getKey(), session.getValue());
		}
		return mockHttpSession;
	}
	
	public static MockHttpSession createMockItemSession() {
		Item item = new Item();
		item.setId(15);
		Map<String, Object> sessionMap = new LinkedHashMap<String, Object>();
		sessionMap.put("item", item);
		return createMockHttpSession(sessionMap);
	}
}
