package com.ssafy.post.dto;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Post {
	private int post_id;
	private String title;
	private String create_date;
	private String update_date;
	private String contents;
	private int type;
	private int like_cnt;
	private int cate_mid_id;
	private int user_id;
	private int price;
	private int del_flag;
	private int deal_type;
	private int deal_weak;

	private String tags;
	private String files;
	
	//07.28
	private List<MultipartFile> file;
	private String cate_big_name;
	private String cate_mid_name;
	private String cate_big_id;
	
	private List<String> deleteFiles;
	
	//08.04
	private String nick_name;
	private String profile_img;
	
	//08.05
	private String likeFlag;
}
