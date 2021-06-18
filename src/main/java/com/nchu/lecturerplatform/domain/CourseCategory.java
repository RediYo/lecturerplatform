package com.nchu.lecturerplatform.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class CourseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;

    //课程类别与课程
    @OneToMany(mappedBy = "courseCategory", cascade = CascadeType.ALL)
    @JsonIgnore//json对象序列化时忽略
    private List<Course> category_courseList = new ArrayList<>();

    /*
     * 配置课程分类到学生的多对多关系
     * */
    @JsonIgnore//json对象序列化时忽略
    @ManyToMany(mappedBy = "courseCategoryList", cascade = CascadeType.ALL) // 放弃维护中间表
    private List<Student> students= new ArrayList<>();
}
