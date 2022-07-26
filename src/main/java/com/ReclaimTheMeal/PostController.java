package com.ReclaimTheMeal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostController {
    @Autowired    
    private PostService postService; 
    
    @Autowired
	private UserRepository repo;

    @GetMapping("/add")
    public String showNewPostForm(Model model) {
        Post Post = new Post();
        String name = (SecurityContextHolder.getContext().getAuthentication()).getName();
		User user = repo.findByEmail(name);
        long id = user.getId();
        model.addAttribute("id", id);
        model.addAttribute("post", Post);
        return "postform";
    }
  
    @PostMapping("/posts/save")
    public String savePost(@ModelAttribute("post") Post post) {
        // save Post to database
        try {
            postService.savePost(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
  
    @GetMapping("/posts/update/{id}")
    public String showFormForUpdate(@PathVariable( value = "id") long id, Model model) {
  
        Post post = postService.getPostById(id);
        post.setTimeStart(s.DateTimeToString((post.getStartTime())));
        post.setTimeEnd(s.DateTimeToString((post.getEndTime())));
        model.addAttribute("post", post);
        model.addAttribute("id", id);
        return "posteditform";
    }
  
    @GetMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable (value = "id") long id) {
       // Model model = model.addAttribute("id", id);
        this.postService.deletePostById(id);
        return "redirect:/";
    }
    

    @GetMapping("/posts/user/{id}/posts")
    public String getPostsByUser(@PathVariable(value = "id") Long id, Model model){
        List<Post> posts = postService.getPostsByUser(id);
        model.addAttribute("listPosts", posts);
        model.addAttribute("id", id);
        return "view_my_posts";
    }

    StringConversion s = new StringConversion();
}
