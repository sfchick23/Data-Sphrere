package ru.sfchick.Data_Sphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private int id;

    private String title;

    private String content;

    private String imagePath;

    private String author;

    private int authorId;

    private PostType postType;
}
