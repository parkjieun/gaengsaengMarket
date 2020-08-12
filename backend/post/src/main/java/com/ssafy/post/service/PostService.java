package com.ssafy.post.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.post.dto.Post;

public interface PostService {
	
	public int insertPost(Post dto) throws Exception;

	public int insertHashtag(int num, String[] tags) throws Exception;

	public int deletePost(int post_id)  throws Exception;

	public List<Post> selectAllPost(String query, String user_id, boolean like, int type, int sno, List<String> tags)  throws Exception;

	public Post detailPost(int post_id,String user_id)  throws Exception;

	public int updatePost(Post dto)  throws Exception;

	public int updateHashtag(int post_id, String[] tags)  throws Exception;

	public int insertPostImg(int num, List<MultipartFile> files, String realPath) throws Exception;

	public int deletePostImg(int post_id, List<String> deleteFiles)  throws Exception;
	
	public String selectLike(int post_id, String user_id)  throws Exception;
	
	public int updatePostType(int post_id, int price, String user_id, String gubun)  throws Exception;
	

}
