package com.ssafy.repository;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.model.User;

public interface UserRepository extends JpaRepository<User, String>{
	public boolean existsBySocialId(String socialId);
	public User findBySocialId(String userId);
	@Query(value="select user_id from user where social_id = :socialId",nativeQuery=true)
	public String findId(@Param("socialId") String socialId);

}
