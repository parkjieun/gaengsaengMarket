package com.ssafy.post.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.ssafy.post.dto.CategoryBig;
import com.ssafy.post.dto.CategoryMid;

@Repository
@Mapper
public interface CategoryMapper {
	public List<CategoryBig> selectAllCategoryBig() throws Exception;

	public List<CategoryMid> selectAllCategoryMid(String category_big_id) throws Exception;
}
