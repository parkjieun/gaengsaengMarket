package com.ssafy.post.dto;

import lombok.Data;

@Data
public class Reply {
	private int reply_id;
	private String create_date;
	private String contents;
	private int post_id;
	private String user_id;
	private int del_flag;
	private String nick_name;
	private String profile_img;
}
