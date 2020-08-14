package com.ssafy.post.controller;

 
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.post.dto.CategoryBig;
import com.ssafy.post.dto.CategoryMid;
import com.ssafy.post.dto.Post;
import com.ssafy.post.service.CategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
//http://localhost:8080/swagger-ui.html
@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/post/category")
@Api(value = "SSAFY")
public class CategoryController {

	public static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
	public static final String SUCESS = "success";
	public static final String FAIL = "fail";
	
 
	
	@Autowired
	private CategoryService categoryService;

	 
	@ApiOperation(value = "카테고리 대분류 조회")
	@RequestMapping(value ="/category_big", method = RequestMethod.GET)
	public ResponseEntity<List<CategoryBig>> selectAllCategoryBig() throws Exception {
		logger.info("-------------selectAllCategoryBig-----------------------------");
		List<CategoryBig> list =categoryService.selectAllCategoryBig();
		//System.out.println(">>>"+list);
		return new ResponseEntity<List<CategoryBig>>(list, HttpStatus.OK);
	}
	
	@ApiOperation(value = "카테고리 중분류 조회")
	@RequestMapping(value ="/category_mid/{category_big_id}", method = RequestMethod.GET)
	public ResponseEntity<List<CategoryMid>> selectAllCategoryMid(@PathVariable("category_big_id") String category_big_id) throws Exception {
		logger.info("-------------selectAllCategoryMid-----------------------------");
		List<CategoryMid> list =categoryService.selectAllCategoryMid(category_big_id);
		//System.out.println(">>>"+list);
		return new ResponseEntity<List<CategoryMid>>(list, HttpStatus.OK);
	}
 
	@ApiOperation(value = "카테고리 mid 전체 조회")
	@RequestMapping(value ="/{category_mid_id}",  method = RequestMethod.GET)
	public ResponseEntity<List<Post>> selectCategoryPost(@PathVariable("category_mid_id") String category_mid_id,
			@RequestParam(value = "sno",required = false, defaultValue = "0" ) int sno) throws Exception {
		logger.info("-------------selectCategoryPost-----------------------------");
		List<Post> list =categoryService.selectCategoryPost(category_mid_id, sno);
		Post p = categoryService.selectCateName(category_mid_id);
		list.add(0, p);
		System.out.println(">>>"+list);
		
		return new ResponseEntity<List<Post>>(list, HttpStatus.OK);
	}
	
	@ApiOperation(value = "카테고리 big 전체 조회")
	@RequestMapping(value ="/big/{cate_big_id}",  method = RequestMethod.GET)
	public ResponseEntity<List<Post>> selectCategoryPostByBig(@PathVariable("cate_big_id") String cate_big_id,
			@RequestParam(value = "sno",required = false, defaultValue = "0" ) int sno) throws Exception {
		logger.info("-------------selectCategoryPostByBig-----------------------------");
		List<Post> list =categoryService.selectCategoryPostByBig(cate_big_id, sno);
		System.out.println(">>>"+list);
		
		return new ResponseEntity<List<Post>>(list, HttpStatus.OK);
	}
}
