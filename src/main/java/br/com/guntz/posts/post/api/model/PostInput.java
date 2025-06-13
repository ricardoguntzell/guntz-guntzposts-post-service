package br.com.guntz.posts.post.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostInput {

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    @NotBlank
    private String author;

}
