package br.com.guntz.posts.post.api.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class PostProducedData {

    private UUID postId;

    private String postBody;

}
