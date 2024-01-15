package com.alabi.app.entity;

import java.util.Collection;

public record UserDTO(
		Long id,
		String firstName,
		String lastName,
	    String email,		
		String password,
		Collection <Role> roles,
		String reg_verification_token
		) {

}
