package com.ReclaimTheMeal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private UserRepository repo;
	
	
	@Autowired
	HttpSession httpSession;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		email = request.getParameter("email");
		String password = request.getParameter("password");
		User user = repo.findByEmail(email);
		BCryptPasswordEncoder bcryptPassEncoder = new BCryptPasswordEncoder();
		password = bcryptPassEncoder.encode(password);
		if(user == null)
		//if(user == null || !password.equals(user.getPassword()))
		{
			throw new UsernameNotFoundException("user not found");
		}
		httpSession.setAttribute("username", email);
		return new CustomUserDetails(user);
	}
	

}

