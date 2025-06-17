package br.com.guntz.posts.post.api.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
public class PostOutput {

    private UUID id;

    private String title;

    private String body;

    private String author;

    private Long wordCount;

    private BigDecimal calculatedValue;

}
