package ru.sfchick.PostMicroservice_DataSphere.service;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.sfchick.PostMicroservice_DataSphere.model.Post;
import ru.sfchick.PostMicroservice_DataSphere.repositories.PostRepository;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post findById(int id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public List<Post> findAllByAuthor(String author) {
        return postRepository.findAllByAuthor(author);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public void save(Post post) {
        if (post.getContent() != null) {
            String processedContent = post.getContent().replace("<EOL>", "\n");
            post.setContent(processedContent);
        }
        postRepository.save(post);
    }


}
