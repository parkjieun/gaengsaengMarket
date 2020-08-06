package com.ssafy.post.controller;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern.Flag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.post.dto.BasicResponse;
import com.ssafy.post.dto.Post;
import com.ssafy.post.dto.Reply;
import com.ssafy.post.service.PostService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
//http://localhost:8080/swagger-ui.html
@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/post")
@Api(value = "SSAFY")
public class PostController {

	public static final Logger logger = LoggerFactory.getLogger(PostController.class);
	public static final String SUCESS = "success";
	public static final String FAIL = "fail";
	
	@Value("${file.upload.directory}")
	String uploadFileDir;

	
	@Autowired
	private PostService postService;

	
	@ApiOperation(value = "게시글 작성")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> insertPost(HttpServletRequest req, Post dto, @RequestParam(value="tag", required=false) String[] tags) throws Exception {
		logger.info("-------------insertPost-----------------------------");
		System.out.println(">>>"+dto.toString());
		dto.setContents(dto.getContents().replace("\n","<br>"));
		
		//게시글 등록
		int num = postService.insertPost(dto);
		System.out.println(">>>num "+num);
		int flag = num;
		
		//해쉬태그 등록
		if(tags != null) {
			flag = postService.insertHashtag(num, tags);
			System.out.println(">>>hashatg flag "+flag);
		}
		
		//파일 업로드 & 등록
		if( dto.getFile() != null && dto.getFile().size()!=0 ) {
			System.out.println(">>>."+dto.getFile());
			String realPath = req.getSession().getServletContext().getRealPath(uploadFileDir);
			flag = postService.insertPostImg(num, dto.getFile(), realPath);
			System.out.println(">>>file flag "+flag);
		}
		
		if (flag == 0) {
			return new ResponseEntity<String>(FAIL, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<String>(SUCESS, HttpStatus.OK);
	}   
	
//	@ApiOperation(value = "게시글 작성 테스트")	
//	@RequestMapping(value = "/test", method = RequestMethod.POST)
//	public ResponseEntity<String> insertPost2(HttpServletRequest req, Post dto, int gubun) throws Exception {
//		logger.info("-------------insertPost2-----------------------------");
//		System.out.println(">>>>>>>>>>>getFile: "+dto.getFile());
//		int flag= 0;
//		//파일 업로드 & 등록
//		if(dto.getFile().size()!=0 && dto.getFile() != null) {
//			System.out.println(">>>."+dto.getFile());
//			if(gubun ==1 ) {
//				String realPath = "/home/front/s03p12a504/backend/post/src/main/webapp"+uploadFileDir;
//				flag = postService.insertPostImg(dto.getPost_id(), dto.getFile(), realPath);
//			}else if(gubun ==2) {
//				String realPath =  req.getSession().getServletContext().getRealPath(uploadFileDir);
//				flag = postService.insertPostImg(dto.getPost_id(), dto.getFile(), realPath);
//			}
//			
//		}
//		
//		if (flag == 0) {
//			return new ResponseEntity<String>(FAIL, HttpStatus.NOT_FOUND);
//		}
//		
//		return new ResponseEntity<String>(SUCESS, HttpStatus.OK);
//	}
	  
	
	@ApiOperation(value = "멀티파일 업로드 테스트")
	@RequestMapping(value = "/upload", method = RequestMethod.POST) //@RequestParam("file")  List<MultipartFile> files
	public List<String> upload( Post dto) throws Exception {
		
		List<String> list = new ArrayList<>();
		String path = "D:\\workspace\\post\\src\\main\\webapp\\WEB-INF\\";
				
		for (MultipartFile file : dto.getFile()) {
			String originalFileName = file.getOriginalFilename();
			String safeFile = path + System.currentTimeMillis() + "_" +originalFileName;
			
			try {
				file.transferTo(new File(safeFile));
			}catch(IllegalStateException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}

	@ApiOperation(value = "게시글 삭제")
	@RequestMapping(value ="/{post_id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deletePost(@PathVariable int post_id) throws Exception {
		logger.info("-------------deletePost-----------------------------");

		int flag = postService.deletePost(post_id);

		if (flag == 0) {
			return new ResponseEntity<String>(FAIL, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(SUCESS, HttpStatus.OK);
	}
	
	@ApiOperation(value = "게시글 전체 조회")
	@RequestMapping( method = RequestMethod.GET)
	public ResponseEntity<List<Post>> selectAllPost(String query, String user_id, boolean like, 
			@RequestParam(value = "type",required = false, defaultValue = "1" ) int type,
			@RequestParam(value = "sno",required = false, defaultValue = "0" ) int sno) throws Exception {
		logger.info("-------------selectAllPost-----------------------------");
		System.out.println(query+"/"+user_id+"/"+like+"/"+type+"/"+sno);
		
		List<Post> list =postService.selectAllPost(query,user_id,like,type,sno);
		//System.out.println(">>>"+list);
		return new ResponseEntity<List<Post>>(list, HttpStatus.OK);
	}
	
	@ApiOperation(value = "특정 게시글 조회")
	@RequestMapping(value ="/{post_id}", method = RequestMethod.GET)
	public ResponseEntity<Post> detailPost(@PathVariable int post_id, String user_id) throws Exception {
		logger.info("-------------detailPost-----------------------------");
		System.out.println(post_id+"/"+user_id);
		
		Post post = postService.detailPost(post_id,user_id);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>post:"+post.toString());
		return new ResponseEntity<Post>(post, HttpStatus.OK);
	}
	
	@ApiOperation(value = "게시글 수정")
	@RequestMapping(value = "/{post_id}", method = RequestMethod.PUT)
	public ResponseEntity<String> updatePost(@PathVariable int post_id, HttpServletRequest req, Post dto,
			@RequestParam(value = "tag", required = false) String[] tags) throws Exception {
		logger.info("-------------updatePost-----------------------------");
		
		dto.setContents(dto.getContents().replace("\n","<br>"));
		
		// 게시글 수정
		int flag = postService.updatePost(dto);

		// 해쉬태그 등록
		if (tags != null) {
			flag = postService.updateHashtag(dto.getPost_id(), tags);
		}
		
 
		// 파일 업로드 & 등록
		if (dto.getFile() != null) {
			String realPath = req.getSession().getServletContext().getRealPath(uploadFileDir);
			//flag = postService.updatePostImg(dto.getPost_id(), dto.getFile(), realPath, dto.getDeleteFiles());
			flag = postService.insertPostImg(post_id, dto.getFile(), realPath);
		}
		
		//파일 삭제
		if (dto.getDeleteFiles().size()!=0 && dto.getDeleteFiles() != null) {
			System.out.println("삭제되는 이미지 리스트들");
			System.out.println("getDeleteFiles >>>>"+dto.getDeleteFiles().toString());
			postService.deletePostImg(post_id, dto.getDeleteFiles());
		}

		if (flag == 0) {
			return new ResponseEntity<String>(FAIL, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<String>(SUCESS, HttpStatus.OK);
	}
	
//	@ApiOperation(value = "좋아요 클릭")
//	@RequestMapping(value ="/like", method = RequestMethod.POST)
//	public ResponseEntity<String> insertLikePost(String post_id, String user_id) throws Exception {
//		logger.info("-------------insertLikePost-----------------------------");
//
//		int flag = postService.insertLikePost(post_id, user_id);
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>flag:"+flag);
//		
//		return new ResponseEntity<String>(SUCESS, HttpStatus.OK);
//	}
//	
//	@ApiOperation(value = "좋아요 취소")
//	@RequestMapping(value ="/like", method = RequestMethod.DELETE)
//	public ResponseEntity<String> deleteLikePost(String post_id, String user_id) throws Exception {
//		logger.info("-------------deleteLikePost-----------------------------");
//
//		int flag = postService.deleteLikePost(post_id, user_id);
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>flag:"+flag);
//		
//		return new ResponseEntity<String>(SUCESS, HttpStatus.OK);
//	}
	
	@ApiOperation(value = "좋아요 클릭/취소")
	@RequestMapping(value ="/doLike", method = RequestMethod.POST)
	public ResponseEntity<String> doLike(String post_id, String user_id) throws Exception {
		logger.info("-------------doLike-----------------------------");

		String result = postService.selectLike(post_id, user_id);
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>result:"+result);
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
		
}
