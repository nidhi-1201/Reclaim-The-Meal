package com.ReclaimTheMeal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	public String viewHomePage(Model model)
	{
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = "";
		if (principal instanceof UserDetails) {
		  username = ((UserDetails)principal).getUsername();
		} else {
		  username = principal.toString();
		}
        model.addAttribute("username", username);
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
			return "redirect:/";
		}
		else {
			return "login";
		}
	}

	@GetMapping("/users/update/{id}")
    public String showFormtoUpdateUser(@PathVariable( value = "id") long id, Model model) {
  
        User user = repo.getReferenceById(id);
        model.addAttribute("user", user);
        return "usereditform";
    }

	@GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable (value = "id") long id) {
  
        this.repo.deleteById(id);
        return "redirect:/";
    }

	@PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        try {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodedPassword = encoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			Role role = rolesRepo.getReferenceById(2L);
			user.addRole(role);
			repo.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
}