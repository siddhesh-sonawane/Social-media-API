package com.social.application.Controller;

import com.social.application.DTOs.PostDTO;
import com.social.application.Models.Post;
import com.social.application.Models.User;
import com.social.application.Services.PostService;
import com.social.application.Utility.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostDTO>>> getAllPosts(){

        List<PostDTO> posts = postService.getAllPosts();

        return ResponseEntity.ok(
                ApiResponse.success("Posts fetched", posts)
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Post>> createPost(
            @RequestParam Long userId,
            @RequestParam String caption,
            @RequestParam(required = false) MultipartFile image
    ) {
        try {
            Post result = postService.insertPost(userId, caption, image);
            return ResponseEntity.status(201)
                    .body(ApiResponse.success("Post created", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), "POST_CREATE_FAILED"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Post>> fetchByPostId(@PathVariable long id){

        Post post = postService.fetchPostById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Post fetched", post)
        );
    }

    //this is get mapping for userid
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<PostDTO>>> getPostsByUserId(@PathVariable Long userId){

        List<PostDTO> posts = postService.getPostsByUserId(userId);

        return ResponseEntity.ok(
                ApiResponse.success("User posts fetched", posts)
        );
    }


    @GetMapping("/postWithComment")
    public List<PostDTO>getPostsWithComments(){
        return postService.getAllPostsWithCommentsAndLikes();
    }




//    @GetMapping
//    public ResponseEntity<ApiResponse<List<PostDTO>>> getPosts(){
//
//        List<PostDTO> posts = postService.getAllPosts();
//
//        return ResponseEntity.ok(
//                ApiResponse.success("Posts fetched successfully", posts)
//        );
//    }
}
