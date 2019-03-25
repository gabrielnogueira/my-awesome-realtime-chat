package com.montreal.chat.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "CHAT_MESSAGE")
@NamedQueries({
	@NamedQuery(name = "Message.findRelated", query = "select m from Message m where (m.from.id = ?1 or m.to.id =?1) and (m.from.id = ?2 or m.to.id =?2 or m.to.id = null)"),
	@NamedQuery(name = "Message.findPublic", query = "select m from Message m where m.to is null") 
})

public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_id_sequence")
	@SequenceGenerator(name = "message_id_sequence", sequenceName = "MESSAGE_ID_SEQ")
	private Long id;

	@Column(nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_from_id", nullable = false)
	private User from;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_to_id")
	private User to;

	@Column
	@CreationTimestamp
	private LocalDateTime createdDate;

	public Long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	public User getTo() {
		return to;
	}

	public void setTo(User to) {
		this.to = to;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public Message withContent(String content) {
		this.content = content;
		return this;
	}

	public Message withFrom(User user) {
		this.from = user;
		return this;
	}

	public Message withTo(User user) {
		this.to = user;
		return this;
	}
}