package com.ssafy.post.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.post.dto.CategoryBig;
import com.ssafy.post.dto.CategoryMid;
import com.ssafy.post.dto.Post;
import com.ssafy.post.mapper.CategoryMapper; 
@Service
@Transactional
public class CategpryServiceImpl implements CategoryService {

	@Autowired
	private CategoryMapper mapper;
	
	@Override
	public List<CategoryBig> selectAllCategoryBig() throws Exception {
		// TODO Auto-generated method stub
		return mapper.selectAllCategoryBig();
	}

	@Override
	public List<CategoryMid> selectAllCategoryMid(String category_big_id) throws Exception {
		// TODO Auto-generated method stub
		return mapper.selectAllCategoryMid(category_big_id);
	}

	@Override
	public List<Post> selectCategoryPost(String category_mid) throws Exception {
		return mapper.selectCategoryPost(category_mid);
	}

}
