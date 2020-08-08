package com.ssafy.post.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.post.dto.Reply;
import com.ssafy.post.service.ReplyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
//http://localhost:8080/swagger-ui.html
@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/post/reply")
@Api(value = "SSAFY")
public class ReplyController {

	public static final Logger logger = LoggerFactory.getLogger(ReplyController.class);
	public static final String SUCESS = "success";
	public static final String FAIL = "fail";
	
	
	@Autowired
	private ReplyService replyService;

	@ApiOperation(value = "댓글 등록")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Reply> insertReply(@RequestBody Reply dto) throws Exception {
		logger.info("-------------insertReply-----------------------------");
		System.out.println(">>>>>>>>"+dto.toString());
		
		//댓글 등록
		int reply_id = replyService.insertReply(dto);
		System.out.println(">>>>>>>>>>reply_id: "+reply_id);
		
		//추가한 dto 받아오기
		Reply reply = replyService.selectOneReply(reply_id);
		
		return new ResponseEntity<Reply>(reply, HttpStatus.OK);
	}
	
	@ApiOperation(value = "댓글 삭제")
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteReply(int reply_id) throws Exception {
		logger.info("-------------deleteReply-----------------------------");

		int flag = replyService.deleteReply(reply_id);

		if (flag == 0) {
			return new ResponseEntity<String>(FAIL, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<String>(SUCESS, HttpStatus.OK);
	}
		
	@ApiOperation(value = "댓글 수정")
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<String> updateReply(@RequestBody Reply dto) throws Exception {
		logger.info("-------------updateReply-----------------------------");
		System.out.println(">>>>>>>>"+dto.toString());
		
		int flag = replyService.updateReply(dto);

		if (flag == 0) {
			return new ResponseEntity<String>(FAIL, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(SUCESS, HttpStatus.OK);
	}
	
	@ApiOperation(value = "댓글 전체 조회")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Reply>> selectReply(int post_id) throws Exception {
		logger.info("-------------selectReply-----------------------------");
		List<Reply> lsit = replyService.selectReply(post_id);
		System.out.println(">>>>>>>>"+lsit);

		return new ResponseEntity<List<Reply>>(lsit, HttpStatus.OK);
	}


}
