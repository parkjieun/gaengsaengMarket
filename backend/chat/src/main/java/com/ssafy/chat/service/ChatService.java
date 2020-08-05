package com.ssafy.chat.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.chat.model.ChatMessage;
import com.ssafy.chat.model.ChatRoom;
import com.ssafy.chat.model.ChatRoomJoin;
import com.ssafy.chat.repository.ChatRepository;
import com.ssafy.chat.repository.ChatRoomJoinRepository;
import com.ssafy.chat.repository.ChatRoomRepository;

@Service
public class ChatService {
	@Autowired
	private ChatRoomRepository chatRoomRepository;
	
	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private ChatRoomJoinRepository chatRoomJoinRepository;

    public List<ChatRoom> findAllRoom(String userId) {
        // 채팅방 생성순서 최근 순으로 반환
    	
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByUserId(userId);
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    public Optional<ChatRoom> findRoomById(String roomId) {
        return chatRoomRepository.findById(roomId);
    }

    public ChatRoom createChatRoom(String userId, String receiverId) {
    	System.out.println(userId +","+receiverId);
        ChatRoom chatRoom = new ChatRoom();
        
        chatRoomRepository.save(chatRoom);
        
        ChatRoomJoin chatRoomJoin = new ChatRoomJoin();
        ChatRoomJoin chatRoomJoin2 = new ChatRoomJoin();
        chatRoomJoin.setRoomId(chatRoom.getRoomId());
        chatRoomJoin2.setRoomId(chatRoom.getRoomId());
        
        chatRoomJoin.setUserId(userId);
        chatRoomJoinRepository.save(chatRoomJoin);
        
        chatRoomJoin2.setUserId(receiverId);
        chatRoomJoinRepository.save(chatRoomJoin2);
        
        return chatRoom;
    }
    public List<ChatMessage> getMessage(String roomId){
    	return chatRepository.findAllByRoomId(roomId);
    }
	
}
