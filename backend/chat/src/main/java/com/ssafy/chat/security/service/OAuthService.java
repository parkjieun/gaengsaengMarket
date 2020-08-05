package com.ssafy.chat.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ssafy.chat.model.User;
import com.ssafy.chat.repository.UserRepository;


@Service
public class OAuthService {
	@Autowired
	private UserRepository userRepository;
	private final String KAKAO_URL="https://kapi.kakao.com/v2/user/me";
	private final String GOOGLE_URL = "https://www.googleapis.com/oauth2/v3/userinfo";
	public boolean isOlder(String accessToken,String platform) {
		return userRepository.existsBySocialUid(getSocialIdByOAuth(accessToken,platform));
	}
	
	public String getSocialIdByOAuth(String accessToken,String platform) {
		
		ResponseEntity<?> re =null;
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		String socialId=null;
		
		if(platform.equals("kakao")) {
			headers.add("Authorization", "Bearer " + accessToken);
			re = rt.exchange(KAKAO_URL,HttpMethod.GET,new HttpEntity<String>(headers),String.class);
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(re.getBody().toString());
			socialId = element.getAsJsonObject().get("id").getAsString();
		} 
		else if(platform.equals("google")) {
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GOOGLE_URL)
					.queryParam("access_token", accessToken);
			re = rt.exchange(builder.toUriString(),HttpMethod.GET,new HttpEntity<String>(headers),String.class);
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(re.getBody().toString());
			socialId = element.getAsJsonObject().get("sub").getAsString();
		}
		
		return socialId;
	}

	public String getId(String accessToken, String platform) {
		// TODO Auto-generated method stub
		return userRepository.findId(getSocialIdByOAuth(accessToken, platform));
	}
	
	public String getId(String socialId) {
		if(socialId==null) {
			return "";
		}
		String id = userRepository.findId(socialId);
		if(id==null)
			return "";
		return id;
	}
}
