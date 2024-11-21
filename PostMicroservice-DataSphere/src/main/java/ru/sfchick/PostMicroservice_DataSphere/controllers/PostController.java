package ru.sfchick.PostMicroservice_DataSphere.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sfchick.PostMicroservice_DataSphere.model.Post;
import ru.sfchick.PostMicroservice_DataSphere.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable("id") int id) {
        return postService.findById(id);
    }

    @GetMapping("publication/{author}")
    public List<Post> getPostsByPerson(@PathVariable("author") String author) {
        return postService.findAllByAuthor(author);
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.findAll();
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        System.out.println("Received content: " + post.getContent());
        postService.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }
}
