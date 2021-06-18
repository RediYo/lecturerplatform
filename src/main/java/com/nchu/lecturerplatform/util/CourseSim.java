package com.nchu.lecturerplatform.util;

import lombok.Data;

@Data
public class CourseSim implements Comparable<CourseSim>{

    private Long courseId;
    private Double simScore;

    @Override
    public int compareTo(CourseSim o) {
        return this.simScore.compareTo(o.getSimScore());
    }

    public CourseSim(Long courseId, Double simScore) {
        this.courseId = courseId;
        this.simScore = simScore;
    }
}
