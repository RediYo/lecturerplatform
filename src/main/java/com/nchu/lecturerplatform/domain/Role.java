package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;

    /*
     * 配置角色到学生用户的多对多关系
     * */
    @ManyToMany(mappedBy = "stuRoles", cascade = CascadeType.ALL) // 放弃维护中间表
    private List<Student> students = new ArrayList<>();

    /*
     * 配置角色到讲师用户的多对多关系
     * */
    @ManyToMany(mappedBy = "lecturerRoles", cascade = CascadeType.ALL) // 放弃维护中间表
    private List<Lecturer> lecturers = new ArrayList<>();

    /*
     * 配置角色到管理员用户的多对多关系
     * */
    @ManyToMany(mappedBy = "adminRoles", cascade = CascadeType.ALL) // 放弃维护中间表
    private List<Admin> admins = new ArrayList<>();

    @Override
    public String toString(){
        return "role";
    }

}
