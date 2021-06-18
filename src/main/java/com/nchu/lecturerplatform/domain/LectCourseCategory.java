package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class LectCourseCategory {

    @Id
    private Long categoryId;
    private String category;
    private String username;
}
