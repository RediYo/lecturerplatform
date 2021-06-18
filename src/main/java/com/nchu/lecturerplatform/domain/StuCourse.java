package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "stu_course")
public class StuCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;//学生账号
    /*@Column(name = "course_id")
    private Long courseId;*/
    @Column(columnDefinition = " varchar default '' ")
    private String evaluation;//评价
    @Column(name = "evaluation_state")
    private int evaluationState;//评价状态,如果不设置，默认为0
    @Column(name = "evaluation_stars")
    private int evaluationStars;//评价星级
    private String evaluationTime;
    private Long currentVideoId;
    private Date addTime;

    /**
     * 学生课程对课程 一对一
     */
    @JoinColumn(name = "course_id",referencedColumnName = "id")
    //@OneToOne(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    @OneToOne(targetEntity = Course.class)
    private Course course;

}
