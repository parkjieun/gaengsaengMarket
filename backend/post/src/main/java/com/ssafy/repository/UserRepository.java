package com.ssafy.repository;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	public boolean existsBySocialUid(String socialUid);
	public User findBySocialUid(String socialUid);
	@Query(value="select user_id from user where social_uid = :socialUid",nativeQuery=true)
	public Integer findId(@Param("socialUid") String socialUid);

}
