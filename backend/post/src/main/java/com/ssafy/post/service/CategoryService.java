package com.ssafy.post.service;

import java.util.List;

import com.ssafy.post.dto.CategoryBig;
import com.ssafy.post.dto.CategoryMid;
import com.ssafy.post.dto.Post;

public interface CategoryService {
	public List<CategoryBig> selectAllCategoryBig() throws Exception;

	public List<CategoryMid> selectAllCategoryMid(String category_big_id) throws Exception;

	public List<Post> selectCategoryPost(String category_mid)  throws Exception;
}
