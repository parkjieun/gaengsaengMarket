package com.ssafy.chat.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity

@AllArgsConstructor
public class ChatRoom {
	@Id
	@Column
    private String roomId;
	@Column
    private Date createDate;

    public ChatRoom () {
        this.roomId = UUID.randomUUID().toString();
        this.createDate = new Date();
    }

}