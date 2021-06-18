package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Admin {

    @Id
    private String username;//邮箱作为账户
    private String password;
    private int enabled;//状态：1启用、0禁用

    /*
     * 配置管理员到角色的多对多关系
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
    private List<Role> adminRoles = new ArrayList<>();

    public Admin(String username, String password, int enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public Admin() {

    }
}

