package ru.sfchick.Data_Sphere.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sfchick.Data_Sphere.config.SecurityConfig;
import ru.sfchick.Data_Sphere.dto.PostDTO;
import ru.sfchick.Data_Sphere.dto.PostType;
import ru.sfchick.Data_Sphere.model.Person;
import ru.sfchick.Data_Sphere.service.PeopleService;
import ru.sfchick.Data_Sphere.service.PostService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final SecurityConfig securityConfig;
    private final PeopleService peopleService;

    @Autowired
    public PostController(PostService postService, SecurityConfig securityConfig, PeopleService peopleService) {
        this.postService = postService;
        this.securityConfig = securityConfig;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String posts(Model model) {
        List<PostDTO> posts = postService.getPosts();
        List<Person> people = peopleService.findAll();

        Map<Integer, Person> authorsMap = people.stream()
                .collect(Collectors.toMap(Person::getId, person -> person));

        Person personLogged =  peopleService.findByUsername(securityConfig.getLoggedInUsername())
                .orElseThrow(() -> new RuntimeException("Person not found"));

        model.addAttribute("posts", posts);
        model.addAttribute("authorsMap", authorsMap);
        model.addAttribute("personLogged", personLogged);
        return "posts/posts";
    }


    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") int id, Model model) {
        try {
            Person personLogged =  peopleService.findByUsername(securityConfig.getLoggedInUsername())
                    .orElseThrow(() -> new RuntimeException("Person not found"));

            PostDTO post = postService.getPostById(id);
            model.addAttribute("post", post);
            model.addAttribute("person", peopleService.findById(post.getAuthorId()));
            model.addAttribute("personLogged", personLogged);
            return "posts/post-view";
        } catch (Exception e) {
            model.addAttribute("error", "Post not found or service unavailable");
            return "posts/error";
        }
    }

    @GetMapping("/publication/{author}")
    public String getPostsPerson(@PathVariable("author") String author, Model model) {
        Person person = peopleService.findByUsername(author)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        Person personLogged =  peopleService.findByUsername(securityConfig.getLoggedInUsername())
                .orElseThrow(() -> new RuntimeException("Person not found"));

        model.addAttribute("posts", postService.getPostsByAuthor(author));
        model.addAttribute("person", person);
        model.addAttribute("personLogged", personLogged);
        return "posts/posts-view-by-author";
    }


    @GetMapping("/create")
    public String createPostForm(Model model) {
        Person personLogged =  peopleService.findByUsername(securityConfig.getLoggedInUsername())
                .orElseThrow(() -> new RuntimeException("Person not found"));

        model.addAttribute("post", new PostDTO());
        model.addAttribute("personLogged", personLogged);
        return "posts/post-create";
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadImage(@PathVariable int id) throws IOException {
        return postService.downloadImage(id);
    }


    @PostMapping("/create")
    public String createPost(@ModelAttribute PostDTO post, @RequestParam("file") MultipartFile file) {
        String usernameAuth = securityConfig.getLoggedInUsername();
        post.setAuthor(usernameAuth);
        post.setAuthorId(peopleService.findByUsername(usernameAuth)
                .orElseThrow(() -> new RuntimeException("User not found")).getId());


        if (!file.isEmpty()) {
            String uploadDir = "uploads/" + post.getTitle();
            String fileName = file.getOriginalFilename();

            try {
                Path uploadPath = Paths.get(uploadDir);
                Files.createDirectories(uploadPath);

                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, file.getBytes());
                post.setImagePath(uploadDir + "/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (post.getContent() != null && !post.getContent().isEmpty()) {
                post.setPostType(PostType.TEXT_AND_IMAGE);
            } else {
                post.setPostType(PostType.IMAGE_ONLY);
            }
        } else {
            post.setPostType(PostType.TEXT_ONLY);
        }

        PostDTO createdPost = postService.createPost(post);

        return "redirect:/posts/" + createdPost.getId();
    }

    @DeleteMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") int id) throws IOException {
        String author = securityConfig.getLoggedInUsername();
        postService.deletePost(id);
        return "redirect:/posts/publication/" + author;
    }
}