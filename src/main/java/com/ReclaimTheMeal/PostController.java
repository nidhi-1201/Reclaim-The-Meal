package com.ReclaimTheMeal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
            
    @GetMapping("/")
    public String viewHomePage(Model model) {
        return findPaginated(1, "title", "asc", model);
    }

    @GetMapping("/add")
    public String showNewPostForm(Model model) {
        Post Post = new Post();
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
        return "posteditform";
    }
  
    @GetMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable (value = "id") long id) {
  
        this.postService.deletePostById(id);
        return "redirect:/";
    }

    @GetMapping("/posts/user/{id}/posts")
    public String getPostsByUser(@PathVariable(value = "id") Long id, Model model){
        List<Post> posts = postService.getPostsByUser(id);
        model.addAttribute("listPosts", posts);
        return "redirect:/view_my_posts";
    }

    StringConversion s = new StringConversion();

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
        return "index";
    }
}
