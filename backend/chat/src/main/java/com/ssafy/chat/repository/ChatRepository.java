package com.ssafy.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.chat.model.ChatMessage;
@Repository
public interface ChatRepository extends JpaRepository<ChatMessage,Integer>{
	public List<ChatMessage> findAllByRoomId(String roomId);
}
