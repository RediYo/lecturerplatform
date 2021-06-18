package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Student {

    @Id
    private String username;//邮箱作为账户
    private String nickname;
    private String sex;
    private String profile;
    private String password;
    private int enabled;//状态：1启用、0禁用
    private String imageSite;
    /**
     * 用UUID生成一段数字,发送到用户邮箱，当用户点击链接时,再做一个校验,
     * 如果用户传来的code跟我们发送的code一致，则更改状态为“1”来激活用户.
     */
    private String  code;

    /*
     * 配置学生到角色的多对多关系
     * */
    @ManyToMany(targetEntity = Role.class, cascade = CascadeType.ALL) // 声明表关系
    @JoinTable(
            // 中间表名
            name = "user_role",
            // 当前对象在中间表的外键
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"),
            // 对方对象在中间表的外键
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )    // 配置中间表
    private List<Role> stuRoles = new ArrayList<>();

    /*
     * 配置学生到课程分类的多对多关系-》关系表为category_weight
     * */
    @ManyToMany(targetEntity = CourseCategory.class, cascade = CascadeType.ALL) // 声明表关系
    @JoinTable(
            // 中间表名
            name = "category_weight",
            // 当前对象在中间表的外键
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"),
            // 对方对象在中间表的外键
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )    // 配置中间表
    private List<CourseCategory> courseCategoryList  = new ArrayList<>();


    /*
     * 配置学生和课程的多对多关系
     * */
    @ManyToMany(targetEntity = Course.class, cascade = CascadeType.ALL) // 声明表关系
    @JoinTable(
            // 中间表名
            name = "stu_course",
            // 当前对象在中间表的外键
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"),
            // 对方对象在中间表的外键
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id")
    )    // 配置中间表
    private List<Course> stuCourses = new ArrayList<>();

    /*
     * 配置学生和收藏课程的多对多关系
     * */
    @ManyToMany(targetEntity = Course.class, cascade = CascadeType.ALL) // 声明表关系
    @JoinTable(
            // 中间表名
            name = "course_collection",
            // 当前对象在中间表的外键
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"),
            // 对方对象在中间表的外键
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id")
    )    // 配置中间表
    private List<Course> stuCollections = new ArrayList<>();

    /*
     * 配置学生和讲师的多对多关系
     * */
    @ManyToMany(targetEntity = Lecturer.class) // 声明表关系
    @JoinTable(
            // 中间表名
            name = "student_lecturer",
            // 当前对象在中间表的外键
            joinColumns = @JoinColumn(name = "stu_username", referencedColumnName = "username"),
            // 对方对象在中间表的外键
            inverseJoinColumns = @JoinColumn(name = "lect_username", referencedColumnName = "username")
    )    // 配置中间表
    private List<Lecturer> lecturers = new ArrayList<>();


    /**
     * 课程对订单 一对多
     */
    @OneToMany(mappedBy = "student")
    private List<Order> orderList;

    public Student(String username, String nickname, String sex, String profile, String password, int enabled, String imageSite) {
        this.username = username;
        this.nickname = nickname;
        this.sex = sex;
        this.profile = profile;
        this.password = password;
        this.enabled = enabled;
        this.imageSite = imageSite;
    }

    public Student() {

    }
}
