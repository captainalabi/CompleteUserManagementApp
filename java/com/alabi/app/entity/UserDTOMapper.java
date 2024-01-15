package com.alabi.app.entity;

import java.util.function.Function;

import org.springframework.stereotype.Service;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {

	@Override
	public UserDTO apply(User user) {
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

}
