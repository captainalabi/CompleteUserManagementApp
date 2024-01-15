package com.alabi.app.controller;

import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alabi.app.entity.User;
import com.alabi.app.entity.UserDTO;
import com.alabi.app.service.RoleService;
import com.alabi.app.service.UserService;
import com.alabi.app.utility.EmailMailSenderService;
import com.alabi.app.utility.SiteURL;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {

	
	private final UserService userService;
	
	private final RoleService roleService;
	
	private EmailMailSenderService emailSender;	

	@Autowired
	public UserController(EmailMailSenderService emailSender, UserService userService, RoleService roleService) {		
		this.userService = userService;
		this.roleService = roleService;
		this.emailSender = emailSender;
	}
	
	@GetMapping("/login")
	public String login() {		
		return "login";
	}

	@GetMapping({ "/list-user", "/list" })
	public ModelAndView listUser() {
		ModelAndView mav = new ModelAndView("list-users");
		List<UserDTO> user = userService.read();
		mav.addObject("user", user);
		return mav;
	}

	@GetMapping("/addnewuser")
	public ModelAndView addUserForm() {
		ModelAndView mav = new ModelAndView("register-user-form");
		User user = new User();
		UserDTO userDTO = new UserDTO(user.getId(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getPassword(),
				user.getRoles(),
				user.getRegVerificationToken());
		mav.addObject("userDTO", userDTO);
		mav.addObject("roleList", roleService.readRole());
		return mav;
	}

	@PostMapping("/saveUser")
	public String saveUser(UserDTO userDTO, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		String successMessage = "";
		try {
			if (userDTO.id() != null) {
				userService.edit(userDTO);
				successMessage = "User Edit successful";
			} else {
				String verificationToken = UUID.randomUUID().toString();
				User user = new User();
				user.setFirstName(userDTO.firstName());
				user.setLastName(userDTO.lastName());
				user.setEmail(userDTO.email());
				user.setPassword(userDTO.password());
				user.setRoles(userDTO.roles());
				user.setRegVerificationToken_token(verificationToken);
				
				UserDTO userDTOToSave = new UserDTO(
						user.getId(),
						user.getFirstName(),
						user.getLastName(),
						user.getEmail(),
						user.getPassword(),
						user.getRoles(),
						user.getRegVerificationToken()
						);
				userService.create(userDTOToSave);
				
				String siteUrl = SiteURL.getURL(request);						
				String url = siteUrl + "/verifyEmail?token="+verificationToken;
				
			String subject = "Here's The Link To Confirm Your Email.";
			String body = "<p>Hello,</p>"
						+ "<p>click the link below to complete your registration on User App:</p>"
						+ "<p><b><a href=\"" + url + "\">Confirm Your Email</a></b></p>"
						+ "<p>Ignore this mail if you are not registering.</p>";
				
			emailSender.sendEmail(userDTOToSave.email(), subject, body);
			log.info("after sender ::::::::::::::::::::: ");
				successMessage = "Successful! Please Check Your email To Complete Registration.";				
			}
			
		} catch (DataIntegrityViolationException e) {
			successMessage = "Duplicate Registration, Please Try Again!";
		} catch (AddressException e) {
			successMessage = "Invalid email Address!";
		} catch (MessagingException e) {
			successMessage = "Error Sending message, Please Try Again!";
		}
		redirectAttributes.addFlashAttribute("successMessage", successMessage);
		return "redirect:/addnewuser";
	}

	@GetMapping("/showUpdateForm")
	public ModelAndView showUpdateForm(@RequestParam Long userId) {
		ModelAndView mav = new ModelAndView("register-user-form");
		mav.addObject("roleList", roleService.readRole());
		UserDTO userDTO = userService.findById(userId);
		mav.addObject("userDTO", userDTO);
		return mav;
	}

	@GetMapping("/deleteUser")
	public String deleteEmployee(@RequestParam Long userId) {
		userService.deleteById(userId);
		return "redirect:/list";
	}
	
	@GetMapping("/verifyEmail")
	public String verifyEmail(@Param("token") String token, Model model) {
		boolean verified = userService.verify(token);
		String pageTitle = verified ? "Registration Successful!" : "Registration Failed!";
		model.addAttribute("pageTitle", pageTitle);
		return verified ? "verified_success" : "verified_failed";
	}
}
