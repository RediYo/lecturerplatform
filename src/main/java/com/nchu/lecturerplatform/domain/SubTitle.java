package com.nchu.lecturerplatform.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class SubTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    //private Long topTitleId;
    private String duration;

    //子目录对一级目录
    @JsonIgnore
    @ManyToOne(targetEntity = TopTitle.class)
    @JoinColumn(name = "top_title_id",referencedColumnName = "id") // 配置外键
    private TopTitle topTitle;

    /**
     * 视频子目录对继续学习 一对一
     */
    @JsonIgnore
    @OneToMany(mappedBy="subTitle")
    private List<ContinueStudy> continueStudyList;

    //视频子目录对课堂笔记
    @JsonIgnore
    @OneToMany(mappedBy = "subTitle")
    private List<Note> noteList = new ArrayList<>();

    @Override
    public String toString(){

        return "SubTitle String";
    }
}
