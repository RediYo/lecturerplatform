package com.nchu.lecturerplatform.domain;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class LectFollowView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String lectUsername;
    private String lectNickname;
    private String profile;
    private String lectImageSite;
    private Long followNum;
}
