package com.sis.javaspringbootinterview.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "retrospective")
public class Retrospective {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "summary")
    private String summary;

    @Column(name = "date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @Column(name = "participants")
    @ElementCollection
    private List<String> participants;

    @Column(name = "feedbackItems")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedbackItem> feedbackItems;

    // getters and setters
}