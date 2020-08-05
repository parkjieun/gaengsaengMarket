package com.ssafy.chat.repository;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.chat.model.User;

public interface UserRepository extends JpaRepository<User, String>{
	public boolean existsBySocialUid(String socialUid);
	public User findBySocialUid(String userId);
	@Query(value="select user_id from user where social_uid = :socialUid",nativeQuery=true)
	public String findId(@Param("socialUid") String socialUid);

}
