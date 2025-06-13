package br.com.guntz.posts.post.api.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class PostSummaryOutput {

    private UUID id;

    private String title;

    private String summary;

    private String author;

}
