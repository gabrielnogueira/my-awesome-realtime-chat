package com.montreal.chat.view.dto;

public class ChatMessageDTO {
	private String id;
	private String content;
	private ChatUserDTO userFrom;
	private ChatUserDTO userTo;

	public ChatMessageDTO() {
	}

	public ChatMessageDTO(String content, ChatUserDTO userFrom, ChatUserDTO userTo) {
		this.content = content;
		this.userFrom = userFrom;
		this.userTo = userTo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public ChatUserDTO getUserFrom() {
		return userFrom;
	}

	public void setUserFrom(ChatUserDTO userFrom) {
		this.userFrom = userFrom;
	}
	
	public ChatMessageDTO withId(String id) {
		this.id = id;
		return this;
	}

	public ChatMessageDTO withContent(String content) {
		this.content = content;
		return this;
	}
	
	public ChatMessageDTO fromUser(ChatUserDTO userFrom) {
		this.userFrom = userFrom;
		return this;
	}
	
	public ChatMessageDTO toUser(ChatUserDTO userTo) {
		this.userTo = userTo;
		return this;
	}

	public ChatUserDTO getUserTo() {
		return userTo;
	}

	public void setUserTo(ChatUserDTO userTo) {
		this.userTo = userTo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
