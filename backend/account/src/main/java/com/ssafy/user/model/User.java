package com.ssafy.user.model;

import java.sql.Date;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class User {
	@Id
	@Column(length = 33, nullable = false)
	private String userId;
	@Column
	private String nickName;
	@Column
	private String introduce;
	@Column
	private LocalDateTime createDate;
	@Column(nullable=false)
	private String address;
	@Column
	private String socialId;
	@Column
	private String profileImg;
	@Column
	private int pointVal;
	
	@PrePersist
	public void preProcessing() {
		this.createDate = LocalDateTime.now();
		this.pointVal = 0;
	}

}
