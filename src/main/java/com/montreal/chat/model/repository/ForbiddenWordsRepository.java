package com.montreal.chat.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.montreal.chat.model.entity.ForbiddenWords;

@Repository
public interface  ForbiddenWordsRepository extends  JpaRepository<ForbiddenWords, Long> {
}
