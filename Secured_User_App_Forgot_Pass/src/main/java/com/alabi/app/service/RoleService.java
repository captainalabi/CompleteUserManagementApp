package com.alabi.app.service;

import java.util.List;

import com.alabi.app.entity.RoleDTO;

public interface RoleService {

	void createRole(RoleDTO roleDTO);
	List<RoleDTO> readRole();
	RoleDTO findById(Long id);
	void deleteById(Long id);
}
