package com.ssafy.security.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ssafy.model.User;
import com.ssafy.security.service.OAuthService;
import com.ssafy.util.JwtUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping ("/oauth")
@Slf4j
@CrossOrigin(origins = { "*" }, maxAge = 6000)
@Api(value = "OAUTH")
public class OAuthController {
	@Autowired
	private OAuthService OAuthService;
	
	@Autowired
	private JwtUtil jwtUtil;
	@ApiOperation(value="oauth를 통한 로그인을 진행한다.",response=String.class)
	@PostMapping("/authorization/{platform}")
	@ResponseBody ResponseEntity<String> authGoogle(@RequestHeader String accessToken,@PathVariable String platform){
		
		HttpStatus status = HttpStatus.OK;
		boolean isOlder = OAuthService.isOlder(accessToken,platform);
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("isOlder", ""+isOlder);
		result.put("accessToken", jwtUtil.createToken(OAuthService.getIdByOAuth(accessToken, platform)));

		
		Gson gson = new Gson();
		
		return new ResponseEntity(gson.toJson(result),HttpStatus.OK);
	}
	
}