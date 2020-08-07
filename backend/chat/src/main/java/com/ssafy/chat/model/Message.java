package com.ssafy.chat.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
@Data
public class Message {

	    private String roomId; // 방번호

	    private String userId; // 메시지 보낸사람

	    private String content; // 메시지


}
