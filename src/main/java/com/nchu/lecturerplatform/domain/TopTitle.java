package com.nchu.lecturerplatform.domain;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class TopTitle implements Comparable<TopTitle>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //private Long courseId;
    private String title;

    //主目录与子目录
    @OneToMany(mappedBy = "topTitle", cascade = CascadeType.ALL)
    private List<SubTitle> subTitleList = new ArrayList<>();

    /**
     * 视频主目录对继续学习 一对一
     */
    @OneToMany(mappedBy="topTitle")
    private List<ContinueStudy> continueStudyList;

    //主目录与课程
    @ManyToOne(targetEntity = Course.class)
    @JoinColumn(name = "course_id",referencedColumnName = "id") // 配置外键
    private Course course;

    //视频主目录对课堂笔记
    @OneToMany(mappedBy = "topTitle")
    private List<Note> noteList = new ArrayList<>();


    @Override
    public int compareTo(TopTitle o) {
        //按标题排序
        return this.title.compareTo(o.getTitle());
    }

    @Override
    public String toString(){

        return "TopTitle String";
    }
}
