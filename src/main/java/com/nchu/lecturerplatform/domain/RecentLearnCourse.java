package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class RecentLearnCourse {

    @Id
    private Long courseId;
    private String courseName;
    private String username;//学生用户名
    private Date datetime;

    public RecentLearnCourse(Long courseId, String courseName, String username) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.username = username;
    }

    public RecentLearnCourse() {

    }
}
