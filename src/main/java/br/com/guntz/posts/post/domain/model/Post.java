package br.com.guntz.posts.post.domain.model;

import br.com.guntz.posts.post.api.model.PostReceivedData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Post {

    private static final Integer LIMIT_NUMBER_OF_LINES = 3;

    @EqualsAndHashCode.Include
    @Id
    private UUID id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    private String summary;

    private String author;

    private Long wordCount;

    private BigDecimal calculatedValue;

    public void convertBodyToSummary() {
        String[] lines = getBody().split("\\r?\\n");
        var summaryConcat = new StringBuilder();
        int index = 1;

        for (String line : lines) {
            if (index <= LIMIT_NUMBER_OF_LINES) {
                summaryConcat.append(line.concat("\n"));
            }
            index++;
        }
        setSummary(summaryConcat.toString());
    }

    public void update(PostReceivedData postReceivedData) {
        setWordCount(postReceivedData.getWordCount());
        setCalculatedValue(postReceivedData.getCalculatedValue());
    }
}
