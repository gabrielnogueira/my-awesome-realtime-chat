package com.montreal.chat.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.montreal.chat.model.entity.Message;

@Repository
public interface  MessageRepository extends  JpaRepository<Message, Long> {
	List<Message> findRelated(Long idFrom, Long idTo);
	List<Message> findPublic ();
}
