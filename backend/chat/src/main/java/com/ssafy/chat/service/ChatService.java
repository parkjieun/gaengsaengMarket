package com.ssafy.chat.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

	private final String DELIVERY_URL = "http://info.sweettracker.co.kr";
	
	@Value("${delivery.key}")
	private String deliveryKey;
	
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
        
    	List<ChatRoomJoin> chatRoomSender = chatRoomJoinRepository.findAllByUserId(userId);
    	List<ChatRoomJoin> chatRoomReceiver = chatRoomJoinRepository.findAllByUserId(receiverId);
    	
    	for(ChatRoomJoin crjSender : chatRoomSender) {
    		for(ChatRoomJoin crjReceiver : chatRoomReceiver) {
    			if(crjSender.getRoomId().equals(crjReceiver.getRoomId())) {
    				return null;
    			}
    		}
    	}
    	
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

	public void saveMessage(ChatMessage message) {
		
		chatRepository.save(message);
	}

	public List<ChatRoomJoin> getRoomMember(String roomId) {
		return chatRoomJoinRepository.findAllByRoomId(roomId);
	}

	public String convertMessage(String content) {
		content = content.trim();
		Gson gson = new Gson();
		if(content.matches("^[0-9]{9,25}$")) {
			ResponseEntity<?> re =null;
			RestTemplate rt = new RestTemplate();
			re = rt.exchange(DELIVERY_URL+"/api/v1/recommend?t_key="+deliveryKey+"&t_invoice="+content,HttpMethod.GET,null,String.class);
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(re.getBody().toString());
			System.out.println(re.getBody().toString());
			for(JsonElement je : element.getAsJsonObject().get("Recommend").getAsJsonArray()) {
				StringBuilder sb = new StringBuilder(DELIVERY_URL);
				sb.append("/api/v1/trackingInfo");
				sb.append("?t_key="+deliveryKey);
				sb.append("&t_invoice="+content);
				sb.append("&t_code="+je.getAsJsonObject().get("Code"));
				re = rt.exchange(sb.toString(), HttpMethod.GET,null,String.class);
				System.out.println(je.getAsJsonObject().get("Code").toString() +"확인중 입니다.");
				System.out.println(re.getStatusCode().toString());
				if(re.getStatusCode()==HttpStatus.OK) {
					HashMap<String, String> hm = new HashMap<>();
					hm.put("isDelivery", "true");
					hm.put("code", je.getAsJsonObject().get("Code").getAsString());
					hm.put("content", content);
					return gson.toJson(hm);
				}
			}
		}
		return content;
		
	}
	
}
