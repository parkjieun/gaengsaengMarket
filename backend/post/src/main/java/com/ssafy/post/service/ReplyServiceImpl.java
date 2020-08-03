package com.ssafy.post.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.post.dto.Post;
import com.ssafy.post.dto.Reply;
import com.ssafy.post.mapper.PostMapper;
import com.ssafy.post.mapper.ReplyMapper;

@Service
@Transactional
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyMapper mapper;

	@Override
	public int insertReply(Reply dto) throws Exception {
		return mapper.insertReply(dto);
	}

	@Override
	public int deleteReply(int reply_id) throws Exception {
		return mapper.deleteReply(reply_id);
	}

	@Override
	public int updateReply(Reply dto) throws Exception {
		return mapper.updateReply(dto);
	}

	@Override
	public List<Reply>  selectReply(int post_id) throws Exception {
		return  mapper.selectReply(post_id);
	}


}
