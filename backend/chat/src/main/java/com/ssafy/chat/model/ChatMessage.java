package com.ssafy.chat.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Generated;

import lombok.Data;

@Data
@Entity
public class ChatMessage {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column
	private int messageId;
	@Column
    private String roomId; // 방번호
	@Column
    private String userId; // 메시지 보낸사람
	@Column
    private String content; // 메시지
	@Column
    private Date createDate;
	@Column
	private boolean isRead;
}
