package com.ssafy.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.chat.model.ChatRoomJoin;
@Repository
public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Integer>{

}
