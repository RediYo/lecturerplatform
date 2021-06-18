package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class ShareDoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String site;
    //private Long courseId;
    private Date time;

    //共享资料与课程
    @ManyToOne(targetEntity = Course.class)
    @JoinColumn(name = "course_id",referencedColumnName = "id") // 配置外键
    private Course course;

    public ShareDoc(String name, String site, Course course) {
        this.name = name;
        this.site = site;
        this.course = course;
    }

    public ShareDoc() {

    }
}
