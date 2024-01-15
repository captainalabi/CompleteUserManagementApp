package com.alabi.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.alabi.app.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration{

	private UserService userService;
		
	@Autowired
	public SecurityConfiguration( UserService userService) {
		super();
		this.userService = userService;
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(new Encoder());
		return auth;
	}
	
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		String staticResources = "/images/**";		
		http
		.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests((requests) -> requests
				
				.requestMatchers("/show-create-islamicBibleForm", "/getCreateIslamicBibleForm",
						"/createIslamicBible").
				hasAuthority("ADMIN")
				.requestMatchers("/list-user", "/list", "/showUpdateForm",
						"/deleteUser", "/showRoleForm", "/createRole", "/listRoles",
						"/editRole", "/deleteRole").
				hasAuthority("MASTER")
				.requestMatchers("/login", "/addnewuser", "/saveUser",
						"/forgot_password", "smtp.gmail.com", "mail.smtp.host",
						"/reset_password",
						"/", "/home", "/index", staticResources
						).permitAll()
				.anyRequest().authenticated()
			)
			.formLogin((form) -> form
				.loginPage("/login")
				.permitAll()
				.defaultSuccessUrl("/")
	            .failureUrl("/login?error=true")
			)			
			.logout((logout) -> logout.permitAll()
					.invalidateHttpSession(true)
					.clearAuthentication(true)
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.logoutSuccessUrl("/login?logout")
					.permitAll()
					);
		return http.build();
	}		
}

