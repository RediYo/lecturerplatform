package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class StuLectCourseNews {

    @Id
    @Column(name = "course_id")
    private Long courseId;
    private String stuUsername;
    private String lectUsername;
    private String lectName;
    private String courseName;
    private String courseProfile;
    private int money;
    private String imageSite;
    private Date datetime;

    //学生关注讲师动态与课程
    @ManyToOne(targetEntity = Course.class)
    @JoinColumn(name = "course_id",referencedColumnName = "id",insertable = false,updatable = false) // 配置外键
    private Course course;



}
