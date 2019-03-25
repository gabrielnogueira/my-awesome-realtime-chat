package com.montreal.chat.view.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.montreal.chat.model.entity.User;

public class ChatUserDTO {
	private String id;
	private String name;
	private String email;
	private String cpf;
	private String status;
	private List<ChatUserDTO> contacts;
	private List<ChatMessageDTO> messages;

	public ChatUserDTO() {
	};

	public ChatUserDTO(User user) {
		if (user != null) {
			this.id = String.valueOf(user.getId());
			this.name = user.getName();
			this.email = user.getEmail();
			this.cpf = user.getCpf();
			if (user.getStatus() != null) {
				this.status = user.getStatus().name();
			}
		}
	}

	public ChatUserDTO(User user, Long idParent) {
		this(user);
		if (this.messages == null) {
			this.messages = new ArrayList<ChatMessageDTO>();
		}
		if (user.getMessagesFromMe() != null) {
			this.messages.addAll(user.getMessagesFromMe().stream()
					.filter(message -> message.getTo() != null && message.getTo().getId() == idParent)
					.map(message -> new ChatMessageDTO().withContent(message.getContent())
							.withId(String.valueOf(message.getId())).fromUser(new ChatUserDTO(message.getFrom()))
							.toUser(new ChatUserDTO(message.getTo())))
					.collect(Collectors.toList()));
		}

		if (user.getMessagesToMe() != null) {
			this.messages.addAll(user.getMessagesToMe().stream()
					.filter(message -> message.getFrom() != null && message.getFrom().getId() == idParent)
					.map(message -> new ChatMessageDTO().withContent(message.getContent())
							.withId(String.valueOf(message.getId())).fromUser(new ChatUserDTO(message.getFrom()))
							.toUser(new ChatUserDTO(message.getTo())))
					.collect(Collectors.toList()));
		}

		this.messages.sort(new Comparator<ChatMessageDTO>() {
			@Override
			public int compare(ChatMessageDTO o1, ChatMessageDTO o2) {
				return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
			}
		});

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public List<ChatUserDTO> getContacts() {
		return contacts;
	}

	public void setContacts(List<ChatUserDTO> contacts) {
		this.contacts = contacts;
	}

	public List<ChatMessageDTO> getMessages() {
		return this.messages;
	}

	public void setMessages(List<ChatMessageDTO> messages) {
		this.messages = messages;
	}

	public ChatUserDTO withId(String id) {
		this.id = id;
		return this;
	}

	public ChatUserDTO withName(String name) {
		this.name = name;
		return this;
	}

	public ChatUserDTO withEmail(String email) {
		this.email = email;
		return this;
	}

	public ChatUserDTO withCpf(String cpf) {
		this.cpf = cpf;
		return this;
	}

	public ChatUserDTO withMessages(List<ChatMessageDTO> messages) {
		this.messages = messages;
		return this;
	}

	public ChatUserDTO withContacts(List<ChatUserDTO> contacts) {
		this.contacts = contacts;
		return this;
	}

	public ChatUserDTO withStatus(String status) {
		this.status = status;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
