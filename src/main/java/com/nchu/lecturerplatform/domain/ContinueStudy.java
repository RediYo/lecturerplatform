package com.nchu.lecturerplatform.domain;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ContinueStudy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long continueTime;//单位 秒
    private String username;

    public ContinueStudy(Long continueTime, String username, Course course, TopTitle topTitle, SubTitle subTitle) {
        this.continueTime = continueTime;
        this.username = username;
        this.course = course;
        this.topTitle = topTitle;
        this.subTitle = subTitle;
    }

    /**
     * 继续学习对课程 一对一
     */
    @JoinColumn(name = "course_id",referencedColumnName = "id")
    @ManyToOne(targetEntity = Course.class)
    private Course course;

    /**
     * 继续学习对视频主目录 一对一
     */
    @JoinColumn(name = "top_id",referencedColumnName = "id")
    @ManyToOne(targetEntity = TopTitle.class,cascade = {CascadeType.ALL})
    private TopTitle topTitle;


    /**
     * 继续学习对视频子目录 一对一
     */
    @JoinColumn(name = "sub_id",referencedColumnName = "id")
    @ManyToOne(targetEntity = SubTitle.class,cascade = {CascadeType.ALL})
    private SubTitle subTitle;

    public ContinueStudy() {

    }
}
