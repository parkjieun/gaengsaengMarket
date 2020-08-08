package com.ssafy.post.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.ssafy.post.dto.Reply;

@Repository
@Mapper
public interface ReplyMapper {

	public int insertReply(Reply dto) throws Exception;

	public int deleteReply(int reply_id) throws Exception;

	public int updateReply(Reply dto) throws Exception;

	public List<Reply> selectReply(int post_id) throws Exception;

	public int selectReplyNum() throws Exception;

	public Reply selectOneReply(int reply_id) throws Exception;

}
