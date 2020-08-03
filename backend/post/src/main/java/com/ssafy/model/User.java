package com.ssafy.model;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	@Column
	private String nickName;
	@Column
	private String introduce;
	@Column
	private LocalDateTime createDate;
	@Column(nullable=false)
	private String address;
	@Column
	private String socialUid;
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
