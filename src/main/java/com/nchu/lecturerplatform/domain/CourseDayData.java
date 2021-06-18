package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class CourseDayData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long courseId;
    private String date;
    private int num;

    public CourseDayData(Long courseId, String date, int num) {
        this.courseId = courseId;
        this.date = date;
        this.num = num;
    }

    public CourseDayData() {

    }
}
