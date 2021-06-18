package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String email;
    private Date createTime;
    private int viewState;//默认为0

    public Feedback(String content, String email) {
        this.content = content;
        this.email = email;
    }

    public Feedback() {

    }
}
