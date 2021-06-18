package com.nchu.lecturerplatform.domain;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String name;//真实姓名
    private String sex;
    private String phoneNum;
    private String profile;
    private int viewState;

}
