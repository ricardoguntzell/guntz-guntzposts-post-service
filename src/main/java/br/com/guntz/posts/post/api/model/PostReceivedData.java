package br.com.guntz.posts.post.api.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
public class PostReceivedData {

    private UUID postId;

    private Long wordCount;

    private BigDecimal calculatedValue;

}
