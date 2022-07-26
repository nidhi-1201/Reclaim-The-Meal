package com.ReclaimTheMeal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@Autowired    
    private PostService postService;

	
	@GetMapping("/")
	public String viewHomePage(Model model)
	{
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = "";
		if (principal instanceof UserDetails) {
		  username = ((UserDetails)principal).getUsername();
		} else {
		  username = principal.toString();
		}
		User user = getUserObject();
        model.addAttribute("user", user);
        return findPaginated(1, "title", "asc", model);
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
	@GetMapping("/contact_us")
	public String contactUs(Model model) {
		User user = getUserObject();
		if(user != null) {
            long id = user.getId();
            model.addAttribute("id", id);        	
        }
        else {
        	model.addAttribute("user", user);
        }
		return "contact_us";
	}

	@GetMapping("/about_us")
	public String aboutus(Model model) {
		User user = getUserObject();
		if(user != null) {
            long id = user.getId();
            model.addAttribute("id", id);        	
        }
        else {
        	model.addAttribute("user", user);
        }
		return "about_us";
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
        model.addAttribute("id", id);
        return "update_profile";
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

	public User getUserObject(){
		String name = (SecurityContextHolder.getContext().getAuthentication()).getName();
        
		User u = repo.findByEmail(name);
		return u;		
	}
	
    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        int pageSize = 5;
  
        Page<Post> page = postService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Post> listPosts = page.getContent();
  
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
  
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
  
        model.addAttribute("listPosts", listPosts);
        User user = getUserObject();
        if(user != null) {
            long id = user.getId();
            model.addAttribute("id", id);        	
        }
        else {
        	model.addAttribute("user", user);
        }
        return "index";
    }
}
