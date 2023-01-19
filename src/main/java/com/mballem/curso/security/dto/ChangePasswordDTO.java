package com.mballem.curso.security.dto;

import java.io.Serializable;

import com.mballem.curso.security.validation.FieldsMatch;
import com.mballem.curso.security.validation.Password;

@FieldsMatch(field = "password1", fieldVerify = "password2", message = "As senhas não correspondem!")
public class ChangePasswordDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Password
	private String password1;
	
	@Password
	private String password2;
	
	private String currentPassword;
	
	public ChangePasswordDTO() {

	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	
	
	

}
