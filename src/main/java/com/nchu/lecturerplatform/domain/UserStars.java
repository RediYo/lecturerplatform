package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Data
@Entity
@IdClass(UserStarKey.class)
public class UserStars {

    @Id
    private String u1;
    private Long star1;
    @Id
    private Long ciFirst;
    private String u2;
    private Long star2;
    @Id
    private Long ciSecond;

}
