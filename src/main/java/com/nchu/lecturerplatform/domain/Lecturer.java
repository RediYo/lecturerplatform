package com.nchu.lecturerplatform.domain;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Lecturer {

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
    private int money;
    private int followNum;

    /**
     * 配置讲师到角色的多对多关系
     *
     */
    @ManyToMany(targetEntity = Role.class, cascade = CascadeType.ALL) // 声明表关系
    @JoinTable(
            // 中间表名
            name = "user_role",
            // 当前对象在中间表的外键
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"),
            // 对方对象在中间表的外键
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )    // 配置中间表
    private List<Role> lecturerRoles = new ArrayList<>();

    /*
     * 配置讲师到学生的多对多关系
     * */
    @ManyToMany(mappedBy = "lecturers", cascade = CascadeType.ALL) // 放弃维护中间表
    private List<Student> students= new ArrayList<>();


    //讲师与课程
    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL)
    private List<Course> teachCourses = new ArrayList<>();

    /*//讲师与课程排行 一对多
    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL)
    private List<CourseRanking> courseRankingList = new ArrayList<>();*/

    /*
     * 配置讲师和动态课程的多对多关系
     * */
    @ManyToMany(targetEntity = Course.class, cascade = CascadeType.ALL) // 声明表关系
    @JoinTable(
            // 中间表名
            name = "course_news",
            // 当前对象在中间表的外键
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"),
            // 对方对象在中间表的外键
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id")
    )    // 配置中间表
    private List<Course> lecturerNewsCourses = new ArrayList<>();


    public Lecturer(String username, String nickname, String sex, String profile, String password, int enabled, String imageSite) {
        this.username = username;
        this.nickname = nickname;
        this.sex = sex;
        this.profile = profile;
        this.password = password;
        this.enabled = enabled;
        this.imageSite = imageSite;
    }

    public Lecturer() {

    }

    @Override
    public String toString(){
        return "lecturer";
    }
}
