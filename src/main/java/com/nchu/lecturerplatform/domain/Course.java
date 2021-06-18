package com.nchu.lecturerplatform.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Course{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String profile;
    private String videoSite;
    private String imageSite;
    private String shareDocSite;
    private int enabled;
    private int weekNum;
    private int money;//金额，默认为0，即为免费课程
    //private String username;//所属讲师，spring data jpa已配置故注释掉


    /**
     * 课程对课程排行 一对一
     */
    @JsonIgnore
    @JoinColumn(name = "id",referencedColumnName = "id")
    @OneToOne(targetEntity = CourseRanking.class)
    private CourseRanking courseRanking;


    /**
     * 课程对课程星级排行 一对一
     */
    @OneToOne(mappedBy="course")
    private StarsCourseRanking starsCourseRanking;

    /**
     * 课程对继续学习 一对多
     */
    @OneToMany(mappedBy = "course")
    private List<ContinueStudy> continueStudyList;

    /**
     * 课程对订单 一对多
     */
    @OneToMany(mappedBy = "course")
    private List<Order> orderList;

    //课程与课程视频主目录
    @OneToMany(mappedBy = "course")
    private List<TopTitle> topTitleList = new ArrayList<>();

    //课程与共享资料
    @OneToMany(mappedBy = "course")
    private List<ShareDoc> shareDocList = new ArrayList<>();

    //课程对课堂笔记
    @OneToMany(mappedBy = "course")
    private List<Note> noteList = new ArrayList<>();

    //课程与学生关注讲师动态
    @OneToMany(mappedBy = "course")
    private List<StuLectCourseNews> stuLectCourseNewsList = new ArrayList<>();

    //课程与讲师
    @ManyToOne(targetEntity = Lecturer.class)
    @JoinColumn(name = "username",referencedColumnName = "username") // 配置外键
    private Lecturer lecturer;

    //课程与课程类别
    @ManyToOne(targetEntity = CourseCategory.class)
    @JoinColumn(name = "category_id",referencedColumnName = "id") // 配置外键
    private CourseCategory courseCategory;

    //课程与课程公告
    @OneToMany(mappedBy = "course")//班级删除，公告也全部删除
    private List<Notice> notices = new ArrayList<>();

    //课程与课程评论
    @OneToMany(mappedBy = "course")
    private List<Comment> comments = new ArrayList<>();

    /*
     * 配置课程到学生的多对多关系
     * */
    @ManyToMany(mappedBy = "stuCourses", cascade = CascadeType.ALL) // 放弃维护中间表
    private List<Student> students= new ArrayList<>();

    /*
     * 配置课程动态到讲师的多对多关系，一个讲师可以发布多个课程动态，一个课程动态可以被多个讲师发布 todo 目前只默认为发布新课程动态，之后将添加发布课程公告、课程更新、课程关闭与开启、课程资料上传等更新内容
     * */
    @ManyToMany(mappedBy = "lecturerNewsCourses", cascade = CascadeType.ALL) // 放弃维护中间表
    private List<Lecturer> news_lecturers= new ArrayList<>();

    /*
     * 配置收藏课程到学生的多对多关系
     * */
    @ManyToMany(mappedBy = "stuCollections", cascade = CascadeType.ALL) // 放弃维护中间表
    private List<Student> students_collection= new ArrayList<>();

    @Override
    public String toString(){
        return "Course-toString";
    }

}
