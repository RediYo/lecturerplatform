package com.nchu.lecturerplatform.util;

import lombok.Data;

@Data
public class CoursePredScore implements Comparable<CoursePredScore>{


    private Long courseId;
    private Double predScore;

    @Override
    public int compareTo(CoursePredScore o) {//根据分数排序
        return this.predScore.compareTo(o.getPredScore());
    }

    public CoursePredScore(Long courseId, Double simScore) {
        this.courseId = courseId;
        this.predScore = simScore;
    }

}
