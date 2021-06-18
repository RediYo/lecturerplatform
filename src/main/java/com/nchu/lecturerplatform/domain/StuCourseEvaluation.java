package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class StuCourseEvaluation {

    @Id
    private Long id;
    private String username;
    private String nickname;
    private String imageSite;
    private Long courseId;
    private String evaluation;
    private int evaluationStars;
    private int evaluationState;
    private String evaluationTime;
}
