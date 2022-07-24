package com.ReclaimTheMeal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
	private UserRepository user;

    @Override
    public Post getPostById(long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        Post post = null;
        if (optionalPost.isPresent()) {
            post = optionalPost.get();
        } else {
            throw new RuntimeException("Post not found for id : " + id);
        }
        return post;
    }
  
    @Override
    public void deletePostById(long id) {
        this.postRepository.deleteById(id);
    }
  
    @Override
    public Page<Post> findPaginated(int pageNum, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
  
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return this.postRepository.findAll(pageable);
    }

    StringConversion s = new StringConversion();

    @Override
    public List<Post> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        posts.forEach(post -> post.setTimeStart(s.DateTimeToString((post.getStartTime()))));
        posts.forEach(post -> post.setTimeEnd(s.DateTimeToString((post.getEndTime()))));
        return posts;
    }

    @Override
    public void savePost(Post post) throws Exception {
        String name = (SecurityContextHolder.getContext().getAuthentication()).getName();
        User u = user.findByEmail(name);
        post.setStartTime(s.StringToDateTime(post.getTimeStart()));
        post.setEndTime(s.StringToDateTime(post.getTimeEnd()));
        post.setUser(u);
        this.postRepository.save(post);
    }

    @Override
    public List<Post> getPostsByUser(Long id) {
        return postRepository.findByUserId(id);
    }
}
