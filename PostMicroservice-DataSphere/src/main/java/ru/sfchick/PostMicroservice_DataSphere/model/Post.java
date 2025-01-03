package ru.sfchick.PostMicroservice_DataSphere.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = true)
    private String content;

    @Column(name = "image_path", nullable = true)
    private String imagePath;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "author_id", nullable = false)
    private int authorId;

    @Column(name = "post_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Column(name = "privacy", nullable = false)
    @Enumerated(EnumType.STRING)
    private PrivacyLevel privacy;

}
