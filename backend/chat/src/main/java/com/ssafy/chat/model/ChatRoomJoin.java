package com.ssafy.chat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class ChatRoomJoin {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column
	private int roomJoinId;
	@Column
	private String roomId;
	@Column
	private String userId;

}
