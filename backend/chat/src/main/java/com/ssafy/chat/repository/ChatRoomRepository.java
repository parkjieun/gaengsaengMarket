package com.ssafy.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ssafy.chat.model.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String>{
	@Query(value="select * from chat_room where room_id in (select room_id from chat_room_join where user_id = :userId)",nativeQuery = true)
	public List<ChatRoom> findAllByUserId(String userId);
}
