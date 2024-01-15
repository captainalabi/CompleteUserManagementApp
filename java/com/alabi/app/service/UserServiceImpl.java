package com.alabi.app.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alabi.app.config.Encoder;
import com.alabi.app.entity.Role;
import com.alabi.app.entity.User;
import com.alabi.app.entity.UserDTO;
import com.alabi.app.entity.UserDTOMapper;
import com.alabi.app.exception.UserNotFoundException;
import com.alabi.app.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	
	private final UserRepository userRepository;
	
	private final Encoder passwordEncoder;
	
	private final UserDTOMapper userDTOMapper;
	
	
	@Autowired
public UserServiceImpl(UserRepository userRepository, Encoder passwordEncoder, UserDTOMapper userDTOMapper) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userDTOMapper = userDTOMapper;
	}

//	@Override
//	public User save(UserDTO userDTO) {
//		User user = new User(
//				userDTO.firstName(),
//				userDTO.lastName(), 
//				userDTO.email(),
//				passwordEncoder.encode(userDTO.password()), 
//				userDTO.roles());
//		return userRepository.save(user);
//	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
				if(user != null && user.isEnabled()) {
					return new org.springframework.security.core.userdetails.User(
							user.getEmail(),
							user.getPassword(),
							mapRolesToAuthorities(user.getRoles())
							);
				}else{
					throw new UsernameNotFoundException("Username Not Found!");
				}				
	}

	public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors
				.toList());
	}

	@Override
	public List<UserDTO> read() {
		return userRepository.findAll().stream().map(userDTOMapper).collect(Collectors.toList());
	}

	@Override
	public UserDTO findById(Long id) {
		return userRepository.findById(id).map(userDTOMapper).get();
	}

	@Override
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

	public void create(UserDTO userDTO) {
		User user = new User(
				userDTO.id(), 
				userDTO.firstName(),
				userDTO.lastName(), 
				userDTO.email(),
				passwordEncoder.encode(userDTO.password()), 
				userDTO.roles(),
				userDTO.reg_verification_token());
		userRepository.save(user);
	}

	@Override
	public void edit(UserDTO userDTO) {
		User theUser = userRepository.findById(userDTO.id()).get();
		userRepository.save(
				new User(userDTO.id(), 
						userDTO.firstName(),
						userDTO.lastName(), 
						userDTO.email(),
						theUser.getPassword(),
						userDTO.roles()));
	}

	@Override
	public UserDTO findByEmail(String email) {
		User user = userRepository.findByEmail(email);
		return new UserDTO(
				user.getId(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getPassword(),
				user.getRoles(),
				user.getRegVerificationToken()
				);
	}

	@Override
	public boolean verify(String token) {
		User user = userRepository.findByRegVerificationToken(token);		
		if(user == null || user.isEnabled()) {
			return false;
		}else {
			userRepository.enable(user.getId());
			return true;
		}
	}
	
	@Override
	public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
		User user = userRepository.findByEmail(email);
		if(user != null) {
			user.setResetPasswordToken(token);
			userRepository.save(user);
		}else {
			throw new UserNotFoundException("User With Email: " + email + "Not Found!");
		}		
	}
	
	@Override
	public User get(String token) {
		return userRepository.findByResetPasswordToken(token);
	}
	@Override
	public void updatePassword(User user, String password) {
		String encodedPassword = passwordEncoder.encode(password);
		user.setPassword(encodedPassword);
		user.setResetPasswordToken(null);
		userRepository.save(user);
	}

	@Override
	public void create(User user) {
		userRepository.save(user);
	}
}
