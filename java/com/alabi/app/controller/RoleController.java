package com.alabi.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alabi.app.entity.Role;
import com.alabi.app.entity.RoleDTO;
import com.alabi.app.service.RoleService;

@Controller
public class RoleController {

	private RoleService roleService;

	@Autowired
	public RoleController(RoleService roleService) {
		super();
		this.roleService = roleService;
	}
	
	
	@GetMapping("/showRoleForm")
	public String showRoleForm(Model model) {
		model.addAttribute("role", new Role());
		return "role-form";
	}
	
	@GetMapping("/listRoles")
	public String readRole(Model model) {
		model.addAttribute("roles", roleService.readRole());
		return "list-role";
	}
	
	@PostMapping("/createRole")
	public ModelAndView createRole(RoleDTO roleDTO) {
		roleService.createRole(roleDTO);
		ModelAndView mav = new ModelAndView("redirect:/listRoles");
		return mav;
	}
	
	@GetMapping("/editRole")
	public String updateRole(@RequestParam Long id, Model model) {		
		model.addAttribute("role", roleService.findById(id));
		return "role-form";
	}
	
	@GetMapping("/deleteRole")
	public ModelAndView deleteRole(@RequestParam Long id) {
		ModelAndView mav = new ModelAndView("redirect:/listRoles");
		roleService.deleteById(id);
		return mav;
	}
}
