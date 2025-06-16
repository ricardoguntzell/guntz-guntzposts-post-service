package br.com.guntz.posts.post.api.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class PostData {

    private UUID postId;

    private String postBody;

}
