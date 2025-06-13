package br.com.guntz.posts.post.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Post {

    @EqualsAndHashCode.Include
    @Id
    private UUID id;

    private String title;

    private String body;

    private String author;

    private Long wordCount;

    private Float calculatedValue;

}
