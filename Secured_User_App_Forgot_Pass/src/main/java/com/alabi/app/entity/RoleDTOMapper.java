package com.alabi.app.entity;

import java.util.function.Function;

import org.springframework.stereotype.Service;

@Service
public class RoleDTOMapper implements Function<Role, RoleDTO> {

	@Override
	public RoleDTO apply(Role role) {
		return new RoleDTO(
				role.getId(),
				role.getName()
				);
	}

}
