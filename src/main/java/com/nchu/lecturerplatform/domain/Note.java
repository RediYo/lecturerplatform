package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*@Column(name = "course_id")
    private Long courseId;
    @Column(name = "top_id")
    private Long topId;
    @Column(name = "sub_id")
    private Long subId;*/
    private String content;
    private String username;

    //课堂笔记对课程
    @ManyToOne(targetEntity = Course.class)
    @JoinColumn(name = "course_id",referencedColumnName = "id") // 配置外键
    private Course course;

    //课堂笔记对视频主目录
    @ManyToOne(targetEntity = TopTitle.class)
    @JoinColumn(name = "top_id",referencedColumnName = "id") // 配置外键
    private TopTitle topTitle;

    //课堂笔记对视频子目录
    @ManyToOne(targetEntity = SubTitle.class)
    @JoinColumn(name = "sub_id",referencedColumnName = "id") // 配置外键
    private SubTitle subTitle;

    public Note(String content, String username, Course course, TopTitle topTitle, SubTitle subTitle) {
        this.content = content;
        this.username = username;
        this.course = course;
        this.topTitle = topTitle;
        this.subTitle = subTitle;
    }

    public Note() {

    }
}
