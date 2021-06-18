package com.nchu.lecturerplatform.domain;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CourseRanking {//根据参加数量的排行

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long categoryId;
    private int num;

    @OneToOne(mappedBy="courseRanking")
    private Course course;

    /*//课程排行与讲师
    @ManyToOne(targetEntity = Lecturer.class)
    @JoinColumn(name = "username",referencedColumnName = "username") // 配置外键
    private Lecturer lecturer;*/

    @Override
    public String toString(){
        return "CourseRanking-toString";
    }

}
