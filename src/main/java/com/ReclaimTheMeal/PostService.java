package com.ReclaimTheMeal;

import java.util.List;
import org.springframework.data.domain.Page;

public interface PostService {
    List<Post> getAllPosts();
    void savePost(Post post) throws Exception;
    Post getPostById(long id);
    void deletePostById(long id);
    Page<Post> findPaginated(int pageNum, int pageSize,
                               String sortField,
                               String sortDirection);
}
