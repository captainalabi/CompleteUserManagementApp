package com.alabi.app.controller;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.alabi.app.entity.User;
import com.alabi.app.exception.UserNotFoundException;
import com.alabi.app.service.UserService;
import com.alabi.app.utility.EmailMailSenderService;
import com.alabi.app.utility.SiteURL;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;

@Controller
@AllArgsConstructor
public class ForgotPasswordController {

	@Autowired
	private final UserService userService;
	
	@Autowired
	private final EmailMailSenderService emailMailSenderService;
	
	@GetMapping("/forgot_password")
	public String showForgotPasswordForm(Model model) {
		model.addAttribute("pageTitle", "Forgot Password");
		return "forgot_password_form";
	}
	
	@PostMapping("/forgot_password")
	public String processForgotPasswordForm(HttpServletRequest request,
			Model model) {
		String email = request.getParameter("email");
		String token = RandomString.make(45); 		
		try {
			userService.updateResetPasswordToken(token, email);
			String resetPasswordLink = SiteURL.getURL(request) + "/reset_password?token=" + token;

			String subject = "Here Is The Link To Reset Your Password";
			
			String body = "<p>You have requested to change your password</p>"
							+ "<p>Click the link below to change your password</p>"
							+ "<p><b><a href=\"" + resetPasswordLink + "\">Change your password</a><b></p>"
							+ "<p>Ignore this mail if you remember your passwor or you did not make the request</p>";
			model.addAttribute("message", "We have sent a reset password token to your email, please check.");
			try {
				emailMailSenderService.sendEmail(email, subject, body);
			} catch (AddressException e) {
				model.addAttribute("message", "message in Email Address, Please Try Again");
			} catch (MessagingException e) {
				model.addAttribute("message", "Error Sending Message, Please Try Again");
			}
		} catch (UserNotFoundException e) {
			model.addAttribute("message", e.getMessage());
		}			
		return "forgot_password_form";
	}
	
	@GetMapping("/reset_password")
	public String showResetPasswordForm(@Param(value = "token") String token,
			Model model) {		
		User user = userService.get(token);
		if(user == null) {
			model.addAttribute("error", "Invalid Token!");
			return "message";
		}
		model.addAttribute("token", token);
		model.addAttribute("pageTitle", "Reset Your Password");
		return "reset_password_form";
	}
	
	@PostMapping("/reset_password")
	public String processResetPasswordForm(HttpServletRequest request,
			Model model) {		
		String token = request.getParameter("token");
		User user = userService.get(token);
		
		String password = request.getParameter("password").toString();
		String confirmPassword = request.getParameter("confirmPassword").toString();
		
		if(password.equals(confirmPassword) && !password.equals(null) && !confirmPassword.equals(null)) {
			if(user != null) {
				userService.updatePassword(user, password);
				model.addAttribute("message", "You have successfully changed your password.");
			}else {
				model.addAttribute("pageTitle", "Reset Your Password");
				model.addAttribute("message", "Invalid Token!");
			}
		}else {
			model.addAttribute("message", "Password Did Not Match, Please Try Again!");
		}		
		return "reset_password_form";
	}
}
