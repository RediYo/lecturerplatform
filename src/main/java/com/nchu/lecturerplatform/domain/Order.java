package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createTime;
    private int payStatus;



    //订单对课程
    @ManyToOne(targetEntity = Course.class)
    @JoinColumn(name = "course_id",referencedColumnName = "id") // 配置外键
    private Course course;

    //订单对学生
    @ManyToOne(targetEntity = Student.class)
    @JoinColumn(name = "username",referencedColumnName = "username") // 配置外键
    private Student student;

    public Order(Course course, Student student) {
        this.course = course;
        this.student = student;
    }

    public Order() {

    }
}
