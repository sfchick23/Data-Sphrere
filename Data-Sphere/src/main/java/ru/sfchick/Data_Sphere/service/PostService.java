package ru.sfchick.Data_Sphere.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.sfchick.Data_Sphere.dto.PostDTO;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final RestTemplate restTemplate;

    private final String POST_SERVICE_URL = "http://localhost:8081/posts";

    @Autowired
    public PostService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PostDTO getPostById(int id) {
        return restTemplate.getForObject(POST_SERVICE_URL + "/" + id, PostDTO.class);
    }

    public List<PostDTO> getPostsByAuthor(String author) {
        ResponseEntity<List<PostDTO>> response = restTemplate.exchange(
                POST_SERVICE_URL + "/publication/" + author,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        return response.getBody();
    }

    public List<PostDTO> getPosts() {
        try {
            ResponseEntity<List<PostDTO>> response = restTemplate.exchange(
                    POST_SERVICE_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<PostDTO>>() {}
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                // Логирование ошибки
                throw new RuntimeException("Failed to fetch posts: " + response.getStatusCode());
            }
        } catch (Exception e) {
            // Логирование ошибки
            throw new RuntimeException("Error during communication with post service", e);
        }
    }


    @Transactional
    public PostDTO  createPost(PostDTO postDTO) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (csrfToken != null) {
            headers.set(csrfToken.getHeaderName(), csrfToken.getToken());
        }

        HttpEntity<PostDTO> entity = new HttpEntity<>(postDTO, headers);

        PostDTO createdPost = restTemplate.postForObject(POST_SERVICE_URL, entity, PostDTO.class);

        return createdPost;
    }

    @Transactional
    public void deletePost(int id) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (csrf != null) {
            headers.set(csrf.getHeaderName(), csrf.getToken());
        }

        HttpEntity<PostDTO> entity = new HttpEntity<>(headers);

        restTemplate.delete(POST_SERVICE_URL + "/delete/" + id, entity, PostDTO.class);

    }

}
