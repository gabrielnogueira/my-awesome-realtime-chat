package com.montreal.chat.model.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.montreal.chat.common.Status;
import com.montreal.chat.view.dto.ChatUserDTO;

@Entity
@Table(name="CHAT_USER")
@NamedQuery(name = "User.findByCpfAndEmail", query = "select u from User u where u.cpf = ?1 and u.email = ?2")
public class User {
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "user_id_sequence")
    @SequenceGenerator(name = "user_id_sequence", sequenceName = "USER_WORDS_ID_SEQ")
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String cpf;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@OneToMany(mappedBy = "from", fetch=FetchType.EAGER)
	private Set<Message> messagesFromMe;

	@OneToMany(mappedBy = "to", fetch=FetchType.EAGER)
	private Set<Message> messagesToMe;

	@Column
	@CreationTimestamp
	private LocalDateTime createdDate;

	@Column
	@UpdateTimestamp
	private LocalDateTime lastModifiedDate;

	public User() {
	};

	public User(ChatUserDTO userDto) {
		this.id = Long.parseLong(userDto.getId());
		this.name = userDto.getName();
		this.cpf = userDto.getCpf();
		this.email = userDto.getEmail();
		this.status = Status.valueOf(userDto.getStatus());
	}

	public Long getId() {
		return id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public User withCpf(String cpf) {
		this.cpf = cpf;
		return this;
	}

	public User withEmail(String email) {
		this.email = email;
		return this;
	}

	public User withStatus(Status status) {
		this.status = status;
		return this;
	}

	public User withName(String name) {
		this.name = name;
		return this;
	}

	public User withId(Long id) {
		this.id = id;
		return this;
	}

	public Set<Message> getMessagesFromMe() {
		return messagesFromMe;
	}

	public void setMessagesFromMe(Set<Message> messagesFromMe) {
		this.messagesFromMe = messagesFromMe;
	}

	public Set<Message> getMessagesToMe() {
		return messagesToMe;
	}

	public void setMessagesToMe(Set<Message> messagesToMe) {
		this.messagesToMe = messagesToMe;
	}

}