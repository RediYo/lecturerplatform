package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity

public class CourseNews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Column(name = "course_id")
    private Long courseId;
    private Date datetime;//时间

}
