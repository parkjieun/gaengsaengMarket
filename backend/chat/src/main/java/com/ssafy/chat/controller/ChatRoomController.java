package com.ssafy.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.chat.model.ChatMessage;
import com.ssafy.chat.model.ChatRoom;
import com.ssafy.chat.repository.ChatRoomRepository;
import com.ssafy.chat.service.ChatService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
@Slf4j
@CrossOrigin(origins = { "*" }, maxAge = 6000)
public class ChatRoomController {


	@Autowired
	private ChatService chatService;

	// 모든 채팅방 목록 반환
	@GetMapping("/room")
	@ResponseBody
	public ResponseEntity<List<ChatRoom>> getRoom(Authentication authentication) {
		String userId = authentication.getPrincipal().toString();
		return new ResponseEntity(chatService.findAllRoom(userId),HttpStatus.OK);
	}

	// 채팅방 생성
	@PostMapping("/room")
	@ResponseBody
	public ResponseEntity<ChatRoom> createRoom(Authentication authentication,@RequestParam("receiverId") String receiverId) {
		String userId = authentication.getPrincipal().toString();
		return new ResponseEntity(chatService.createChatRoom(userId,receiverId),HttpStatus.OK);
	}


	@GetMapping("/{roomId}")
	public ResponseEntity<List<ChatMessage>> getMessage(Authentication authentication,@PathVariable String roomId) {
		return new ResponseEntity(chatService.getMessage(roomId),HttpStatus.OK);
	}
}
