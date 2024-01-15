package com.alabi.app.entity;


import java.util.Collection;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {

	@jakarta.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	//@NotBlank(message = "First Name is Mandatory")
	private String firstName;
	
	//@NotBlank(message = "Last Name is Mandatory")
	private String lastName;
	
	@Column(unique = true)
	//@NotBlank(message = "Email is Mandatory") @Email
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@ManyToAny(fetch = FetchType.LAZY)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(
					name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(
					name = "role_id", referencedColumnName = "id"))
	private Collection <Role> roles;
	
	private boolean isEnabled = false;
	
	@Column(name = "reg_verification_token")
	private String regVerificationToken;
	
	@Column(name = "reset_password_token")
	private String resetPasswordToken;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String firstName, String lastName, String email, Collection <Role> roles) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;		
		this.roles = roles;
	}
	
	public User(String firstName, String lastName, String email, Collection <Role> roles, String regVerificationToken) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;		
		this.roles = roles;
		this.regVerificationToken = regVerificationToken;
	}
	
	public User(Long id, String firstName, String lastName, String email, String password, Collection <Role> roles, String regVerificationToken) {
		super();
		
		this.Id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;	
		this.password = password;
		this.roles = roles;
		this.regVerificationToken = regVerificationToken;
	}
	
	public User(String firstName, String lastName, String email, String password, Collection <Role> roles) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}
	
	public User(Long id, String firstName, String lastName, String email, String password, Collection <Role> roles) {
		super();
		Id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getRegVerificationToken() {
		return regVerificationToken;
	}

	public void setRegVerificationToken_token(String regVerificationToken) {
		this.regVerificationToken = regVerificationToken;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	
}
