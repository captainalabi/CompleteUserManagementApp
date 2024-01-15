package com.alabi.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alabi.app.entity.Role;
import com.alabi.app.entity.RoleDTO;
import com.alabi.app.entity.RoleDTOMapper;
import com.alabi.app.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleRepository repository;
	@Autowired
	private final RoleDTOMapper roleDTOMapper;
	
	@Override
	public void createRole(RoleDTO roleDTO ) {
		repository.save(new Role(roleDTO.id(), roleDTO.name()));
	}

	@Override
	public List<RoleDTO> readRole() {
		return repository.findAll().stream().map(roleDTOMapper).collect(Collectors.toList());
	}

	@Override
	public RoleDTO findById(Long id) {
		return repository.findById(id).map(roleDTOMapper).get();
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

}
