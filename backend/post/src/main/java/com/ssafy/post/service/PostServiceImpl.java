package com.ssafy.post.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.post.dto.Post;
import com.ssafy.post.mapper.PostMapper;

@Service
@Transactional
public class PostServiceImpl implements PostService {

	@Autowired
	private PostMapper mapper;

	@Override
	public int insertPost(Post dto) throws Exception {
		
		//post 등록
		int flag = mapper.insertPost(dto);
		
		//글번호 가져오기
		if(flag!=0) {
			flag = mapper.selectPostNum();
		}
		
		return flag;
	}

	@Override
	public int insertHashtag(int num, String[] tags) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("num",num);
		map.put("tags",tags);
		
		System.out.println(">>>>>>>>>>>>>>num:"+num);
		System.out.println(">>>>>>>>>>>>>>tags:"+Arrays.toString(tags));
		
		//해쉬태그 등록
		int flag = mapper.insertHashtag(map);

		return flag;
	}

	@Override
	public int insertPostImg(int num, List<MultipartFile> files, String realPath) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("num",num);
		List<String> imgList = new ArrayList<String>();
		
		//파일 업로드
		List<String> list = new ArrayList<>();
		LocalDateTime time = LocalDateTime.now();
		String md5Hex="";
		
		for (MultipartFile file : files) {
			String originalFileName = file.getOriginalFilename();
			md5Hex = DigestUtils.md5DigestAsHex((time.toString()+originalFileName).getBytes()).toLowerCase();
			//String safeFile = System.currentTimeMillis() + "_" +originalFileName;
			String safeFile =  md5Hex + originalFileName.substring(originalFileName.lastIndexOf("."));
			
			try {
				file.transferTo(new File(realPath + safeFile));
				imgList.add(safeFile);
				
			}catch(IllegalStateException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		
		//파일 등록
		int flag = 0;
		//map.put("imgList", imgList);
		//flag =mapper.insertPostImg(map);
		
		for (String img_url : imgList) {
			System.out.println(">>img_url:"+img_url);
			map.put("img_url",img_url);
			flag = mapper.insertPostImg(map);
		}
		
		return flag;
	}

	@Override
	public int deletePost(int post_id) throws Exception {

		return mapper.deletePost(post_id);
	}

	@Override
	public List<Post> selectAllPost(String query, String user_id, boolean like, int type, int sno, List<String> tags) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("query",query);
		map.put("user_id",user_id);
		map.put("like",like);
		map.put("type",type);
		map.put("sno", sno);
		map.put("tags", tags);
		
		return mapper.selectAllPost(map);
	}

	@Override
	public Post detailPost(int post_id,String user_id) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("post_id",post_id);
		map.put("user_id",user_id);
		
		return mapper.detailPost(map);
	}

	@Override
	public int updatePost(Post dto) throws Exception {
		return mapper.updatePost(dto);
	}

	@Override
	public int updateHashtag(int post_id, String[] tags) throws Exception {
		//기존 해쉬태그 삭제
		int flag = mapper.deleteHashtag(post_id);
		
		//해쉬태그 등록
		insertHashtag(post_id, tags);
		
		return flag;
	}

//	@Override
//	public int updatePostImg(int post_id, List<MultipartFile> files, String realPath, List<String> deleteFiles) throws Exception {
//		int flag =0;
//		
//		//기존 이미지 삭제
//		if(deleteFiles != null) {
//		
//			flag = mapper.deletePostImg(deleteFiles);
//		}
//		//이미지 등록
//		if(files.size() != 0)
//			insertPostImg(post_id, files, realPath);
//		
//		return flag;
//	}
	@Override
	public int deletePostImg(int post_id, List<String> deleteFiles) throws Exception {
		System.out.println(">>>>>>>deleteFiles:"+deleteFiles.size());
		//기존 이미지 삭제
		int flag = mapper.deletePostImg(deleteFiles);
		
		return flag;
	}

	@Override
	public String selectLike(int post_id, String user_id) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("post_id", post_id);
		map.put("user_id", user_id);
		
		int flag = mapper.selectLike(map);
		System.out.println("selectLike flag:"+flag);
		String result = "";
		
		if(flag > 0) {
			flag = mapper.deleteLikePost(map);
			map.put("type","del");
			mapper.updateLikePost(map);
			result = "delete";
		}else {
			flag = mapper.insertLikePost(map);
			map.put("type","ins");
			mapper.updateLikePost(map);
			result = "insert";
		}
		
		return result;
	}

	@Override
	public int updatePostType(int post_id, int price, String user_id, String gubun) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		int flag = mapper.updatePostType(post_id);
		
		if(flag > 0 && gubun.equals("delivery")) {
			map.put("price", price);
			map.put("user_id", user_id);
			flag = mapper.updatePoint(map);
		}
		
		return flag;
	}
	 
}
