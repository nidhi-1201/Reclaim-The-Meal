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
            
    // @RequestMapping("/postform")    
    // public String showPostForm(Model m){    
    //     m.addAttribute("command", new Post());  
    //     return "postform";   
    // }    
    // /*It saves object into database. The @ModelAttribute puts request data  
    //  *  into model object. You need to mention RequestMethod.POST method   
    //  *  because default request is GET*/    
    // @RequestMapping(value="/save",method = RequestMethod.POST)    
    // public String save(@ModelAttribute("post") Post p){    
    //     post.save(p);    
    //     return "redirect:/viewpost";  
    // }    
    // /* It provides list of employees in model object */    
    // @RequestMapping("/viewpost")    
    // public String viewemp(Model m){    
    //     List<Post> list=post.getPosts();    
    //     m.addAttribute("list",list);  
    //     return "viewpost";    
    // }    
    // /* It displays object data into form for the given id.   
    //  * The @PathVariable puts URL data into variable.*/    
    // @RequestMapping(value="/editpost/{id}")    
    // public String edit(@PathVariable int id, Model m){    
    //     Post p = (Post) post.getPostById(id);    
    //     m.addAttribute("command",p);  
    //     return "posteditform";    
    // }    
    // /* It updates model object. */    
    // @RequestMapping(value="/editsave",method = RequestMethod.POST)    
    // public String editsave(@ModelAttribute("post") Post p){    
    //     post.update(p);    
    //     return "redirect:/viewpost";    
    // }    
    // /* It deletes record for the given id in URL and redirects to /viewemp */    
    // @RequestMapping(value="/deletepost/{id}",method = RequestMethod.GET)    
    // public String delete(@PathVariable int id){    
    //     post.delete(id);    
    //     return "redirect:/viewpost";    
    // }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        return findPaginated(1, "title", "asc", model);
    }
  
    @GetMapping("/posts")
    public String viewPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "homepage";
    }

    @GetMapping("/add")
    public String showNewPostForm(Model model) {
        Post Post = new Post();
        model.addAttribute("post", Post);
        return "postform";
    }
  
    @PostMapping("/save")
    public String savePost(@ModelAttribute("post") Post post) {
        // save Post to database
        try {
            postService.savePost(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/posts";
    }
  
    @GetMapping("/update/{id}")
    public String showFormForUpdate(@PathVariable( value = "id") long id, Model model) {
  
        Post post = postService.getPostById(id);
        post.setTimeStart(s.DateTimeToString((post.getStartTime())));
        post.setTimeEnd(s.DateTimeToString((post.getEndTime())));
        model.addAttribute("post", post);
        return "posteditform";
    }
  
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable (value = "id") long id) {
  
        this.postService.deletePostById(id);
        return "redirect:/";
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
