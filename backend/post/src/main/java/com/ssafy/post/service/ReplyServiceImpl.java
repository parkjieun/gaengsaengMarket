package com.ssafy.post.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.post.dto.Reply;
import com.ssafy.post.mapper.ReplyMapper;

@Service
@Transactional
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyMapper mapper;

	@Override
	public int insertReply(Reply dto) throws Exception {
		//reply 등록	
		int flag = mapper.insertReply(dto);
		
		//글번호 가져오기
		if(flag!=0) {
			flag = mapper.selectReplyNum();
		}
		
		return flag;
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

	@Override
	public Reply selectOneReply(int reply_id) throws Exception {
		return mapper.selectOneReply(reply_id);
	}


}
