package com.alabi.app.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.alabi.app.entity.Role;
import com.alabi.app.entity.User;
import com.alabi.app.entity.UserDTO;
import com.alabi.app.exception.UserNotFoundException;

public interface UserService extends UserDetailsService{

	//User save(UserDTO userDTO);
	void create(UserDTO userDTO);
	void create(User user);
	List<UserDTO> read();
	UserDTO findById(Long id);
	void deleteById(Long id);
	void edit(UserDTO userDTO);
	UserDTO findByEmail(String email);
	Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles);
	boolean verify(String token);
	void updateResetPasswordToken(String token, String email) throws UserNotFoundException;
	User get(String token);
	void updatePassword(User user, String password);
	
}
