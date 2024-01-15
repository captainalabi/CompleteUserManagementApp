package com.alabi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.alabi.app.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmail(String email);
	
	@Query("UPDATE User u set u.isEnabled=true where u.id=?1")
	@Modifying
	@Transactional
	void enable(Long id);
	
	@Query("SELECT u from User u where u.regVerificationToken=?1")
	User findByRegVerificationToken(String token);
	
	@Query("SELECT u from User u where u.resetPasswordToken=?1")
	User findByResetPasswordToken(String token);
}
