package com.social.application.Services;


import com.social.application.Models.Like;
import com.social.application.Models.Post;
import com.social.application.Models.User;
import com.social.application.Repositories.LikeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
@Service
public class LikeService {
    private final LikeRepository likeRepository;

    @PersistenceContext
    EntityManager entityManager;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }



    public Like insertLike(Like like){

        Long userId = like.getUser().getId();
        Long postId = like.getPost().getId();

        User user = entityManager.find(User.class, userId);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Post post = entityManager.find(Post.class, postId);
        if(post == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }

        boolean alreadyLiked = likeRepository.existsByUser_IdAndPost_Id(userId, postId);
        if(alreadyLiked){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post already liked by user");
        }

        like.setUser(user);
        like.setPost(post);

        return likeRepository.save(like);
    }

    public List<Like> fetchAllLike(){
        return likeRepository.findAll();
    }

    public Like fetchLikeById(Long id){
        return likeRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Like not found with id: " + id
                        )
                );
    }


}
