package com.ReclaimTheMeal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class AppController {
	@Autowired
	HttpSession httpSession;

	@Autowired
	private UserRepository repo;

	@Autowired
	private RoleRepository rolesRepo;

	@GetMapping("")
	public String viewHomePage()
	{
		return "index";
	}
	@GetMapping("/register")
	public String showSignUpForm(Model model)
	{
	model.addAttribute("user",new User());
	return "signup_form";
	}

	@PostMapping("/process_register")
	public String processRegistration(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	    String encodedPassword = encoder.encode(user.getPassword());
	    user.setPassword(encodedPassword);
	    Role role = rolesRepo.getReferenceById(2L);
	    user.addRole(role);
	   repo.save(user);
	   new EmailSend().send(user.getEmail());
	     
	    return "register_success";
	}

	@GetMapping("/list_users")
	public String viewUsersList(Model model) {
		List<User> listUsers = repo.findAll();
	model.addAttribute("listUsers", listUsers);
		return "users";
	}

	@GetMapping("/custom_login_url")
	public String showLogin() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = "";
		if (principal instanceof UserDetails) {
		  username = ((UserDetails)principal).getUsername();
		} else {
		  username = principal.toString();
		}

		if( username!=null && !username.isEmpty() && !username.equals("anonymousUser")) {
			return "redirect:/posts";
			//return "login";

		}
		else {
			return "login";
		}
	}

	@GetMapping("/emailSend")
	public String sendEmail() {
		new EmailSend().send("thakka95@uwindsor.ca");
		return "posts";
	}

	@GetMapping("/emailVerify/{code}")
	public String verifyEmail(String code) {
		new EmailVerify().verify(code, "thakka95@uwindsor.ca");
		return "posts";
	}

}