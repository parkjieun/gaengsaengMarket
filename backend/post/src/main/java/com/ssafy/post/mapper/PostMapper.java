package com.ssafy.post.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.ssafy.post.dto.Post;

@Repository
@Mapper
public interface PostMapper {

	public int insertPost(Post dto) throws Exception;

	public int selectPostNum() throws Exception;

	public int insertHashtag(HashMap<String, Object> map) throws Exception;

	public int insertPostImg(HashMap<String, Object> map) throws Exception;

	public int deletePost(int post_id)  throws Exception;

	public List<Post> selectAllPost(HashMap<String, Object> map) throws Exception;

	public Post detailPost(HashMap<String, Object> map) throws Exception;

	public int updatePost(Post dto) throws Exception;

	public int deleteHashtag(HashMap<String, Object> map)  throws Exception;

	public int deleteHashtag(int post_id)  throws Exception;

	public int deletePostImg(List<String> deleteFiles) throws Exception;

	public int insertLikePost(HashMap<String, Object> map) throws Exception;

	public int deleteLikePost(HashMap<String, Object> map) throws Exception;
	
	public int selectLike(HashMap<String, Object> map) throws Exception;

	public void updateLikePost(HashMap<String, Object> map) throws Exception;
	
	public int updatePostType(int post_id) throws Exception;
	
	public int updatePoint(HashMap<String, Object> map) throws Exception;
	
	public int updatePointPlus(HashMap<String, Object> map) throws Exception;
}
