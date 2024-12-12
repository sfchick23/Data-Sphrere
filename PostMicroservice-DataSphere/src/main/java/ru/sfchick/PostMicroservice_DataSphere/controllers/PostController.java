package ru.sfchick.PostMicroservice_DataSphere.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.findAllPublicPost();
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable("id") int id) {
        return postService.findById(id);
    }

    @GetMapping("/publication/{author}")
    public List<Post> getPostsByPerson(@PathVariable("author") String author) {
        return postService.findAllByAuthor(author);
    }

    @GetMapping("/publication/{author}/private")
    public List<Post> getPrivatePostsByPerson(@PathVariable("author") String author) {
        return postService.findPrivatePostsByAuthor(author);
    }

    @GetMapping("/publication/{author}/public")
    public List<Post> getPublicPostsByPerson(@PathVariable("author") String author) {
        return postService.findPublicPostsByAuthor(author);
    }

    @GetMapping("/search")
    public List<Post> getPostsByTitle(@RequestParam(name = "query", required = false) String title) {
        return postService.searchByTitle(title);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        postService.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<Post> deletePost(@PathVariable("id") int id) {
        postService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
