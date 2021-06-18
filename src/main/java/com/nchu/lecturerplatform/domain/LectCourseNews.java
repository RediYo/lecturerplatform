package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class LectCourseNews {

    @Id
    private Long courseId;
    private String courseName;
    private String courseProfile;
    private String username;
    private String nickname;
    private String imageSite;
    private Date datetime;
    private int money;
}
