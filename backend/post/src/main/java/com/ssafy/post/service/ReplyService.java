package com.ssafy.post.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.post.dto.Post;
import com.ssafy.post.dto.Reply;

public interface ReplyService {
	
	public int insertReply(Reply dto) throws Exception;

	public int deleteReply(int reply_id) throws Exception;

	public int updateReply(Reply dto) throws Exception;

	public List<Reply>  selectReply(int post_id) throws Exception;

	public Reply selectOneReply(int reply_id) throws Exception;


}
