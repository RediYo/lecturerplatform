package com.nchu.lecturerplatform.domain;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Notice  implements Comparable<Notice>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //private Long courseId;
    private Date datetime;
    private String text;

    //公告与课程
    @ManyToOne(targetEntity = Course.class)
    @JoinColumn(name = "course_id",referencedColumnName = "id") // 配置外键
    private Course course;

    public Notice(Date datetime, String text) {
        this.datetime = datetime;
        this.text = text;
    }

    public Notice() {
    }


    @Override
    public int compareTo(Notice o) {
        //按时间排序
        return this.datetime.compareTo(o.getDatetime());
    }
}
