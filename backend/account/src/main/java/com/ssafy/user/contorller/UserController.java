package com.ssafy.user.contorller;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ssafy.user.model.JoinPayload;
import com.ssafy.user.model.User;
import com.ssafy.user.repository.UserRepository;
import com.ssafy.user.security.service.OAuthService;
import com.ssafy.user.service.UserService;
import com.ssafy.user.util.JwtUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
//http://localhost:8080/swagger-ui.html
@RestController
@RequestMapping("/api/user")
@Slf4j
@CrossOrigin(origins = { "*" }, maxAge = 6000)
@Api(value = "USER")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private OAuthService oAuthService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	@Value("${file.upload.directory}")
	String uploadFileDir;
	
	@ApiOperation(value="유저 정보를 얻어온다.", response = User.class)
	@GetMapping
	public ResponseEntity<User> getUser(Authentication authentication){
		String socialId = authentication.getPrincipal().toString();
		return  new ResponseEntity(userService.getUser(oAuthService.getId(socialId)),HttpStatus.OK);
	}
	
	@ApiOperation(value="회원가입을 진행한다.")
	@PostMapping
	public ResponseEntity<?> insertUser(Authentication authentication,JoinPayload payload, HttpServletRequest request){
		LocalDateTime time = LocalDateTime.now();
		String filename = payload.getImg().getOriginalFilename();
		//String rootPath = request.getSession().getServletContext().getRealPath("/");
		String rootPath = request.getSession().getServletContext().getRealPath(uploadFileDir);
		String attachPath = "resources/upload/";
		
	    String md5Hex = DigestUtils.md5DigestAsHex((time.toString()+payload.getImg().getOriginalFilename()).getBytes()).toLowerCase();
		//String filePath = rootPath + attachPath + md5Hex + filename.substring(filename.lastIndexOf("."));
	    String filePath = rootPath + md5Hex + filename.substring(filename.lastIndexOf("."));
	    
		try {
			payload.getImg().transferTo(new File(filePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		User user = new User();
		String socialId = authentication.getPrincipal().toString();
		user.setSocialUid(socialId);
		user.setProfileImg(uploadFileDir+md5Hex+filename.substring(filename.lastIndexOf(".")));
		user.setAddress(payload.getAddress());
		user.setNickName(payload.getNickName());
		user.setIntroduce(payload.getIntroduce());
		userService.insertUser(user);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@ApiOperation(value="유저 정보를 업데이트 한다.")
	@PutMapping
	public ResponseEntity<?> updateUser(Authentication authentication,@RequestBody User user){
		String socialId = authentication.getPrincipal().toString();
		user.setUserId(oAuthService.getId(socialId));
		userService.updateUser(user);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@ApiOperation(value="타인의 정보를 가져온다.")
	@GetMapping("/{user_id}")
	public ResponseEntity<User> getOtherUser(Authentication authentication,@PathVariable("user_id") int userId){
		if(authentication ==null || oAuthService.getId(authentication.getPrincipal().toString())!=userId ) {
			User user = userService.getOtherUser(userId);
			return new ResponseEntity(user,user==null?HttpStatus.NOT_FOUND:HttpStatus.OK);
		}
		return new ResponseEntity(userService.getUser(userId),HttpStatus.OK);
	}
	
}
