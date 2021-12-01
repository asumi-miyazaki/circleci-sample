package com.example.form;

/**
 * コメント投稿時に使用するフォームクラス.
 * 
 * @author kojiro0706
 *
 */
public class InsertCommentForm {

	/** 商品ID */
	private String itemId;
	/** 名前 */
	private String name;
	/** コンテント */
	private String content;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "InsertCommentForm [itemId=" + itemId + ", name=" + name + ", content=" + content + "]";
	}

}
