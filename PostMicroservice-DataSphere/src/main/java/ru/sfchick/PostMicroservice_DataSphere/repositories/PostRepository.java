package ru.sfchick.PostMicroservice_DataSphere.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sfchick.PostMicroservice_DataSphere.model.Post;
import ru.sfchick.PostMicroservice_DataSphere.model.PrivacyLevel;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByAuthor(String author);

    List<Post> findByTitleContainingIgnoreCase(String title);

    List<Post> findAllByAuthorAndPrivacy(String author, PrivacyLevel privacy);

    List<Post> findAllByPrivacy(PrivacyLevel privacy);

}
