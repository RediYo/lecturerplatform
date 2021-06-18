package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CategoryWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "category_id")
    private Long categoryId;
    private String username;
    private Long weight;

}
