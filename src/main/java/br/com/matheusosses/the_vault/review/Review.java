package br.com.matheusosses.the_vault.review;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long gameId;

    @Column(nullable = false)
    private Integer score;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private LocalDateTime createdAt = LocalDateTime.now();
}
