package com.montreal.chat.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CHAT_FORBIDDEN_WORDS")
public class ForbiddenWords {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forbidden_words_id_sequence")
	@SequenceGenerator(name = "forbidden_words_id_sequence", sequenceName = "FORBIDDEN_WORDS_ID_SEQ")
	private Long id;

	@Column(nullable = false)
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
