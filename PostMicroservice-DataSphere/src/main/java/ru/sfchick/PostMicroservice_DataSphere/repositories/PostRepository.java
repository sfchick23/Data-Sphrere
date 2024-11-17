package ru.sfchick.PostMicroservice_DataSphere.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sfchick.PostMicroservice_DataSphere.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByAuthor(String author);
}
