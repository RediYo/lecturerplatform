package com.nchu.lecturerplatform.util;

import lombok.Data;

import java.util.List;

@Data
public class DayDate {

    private List<String> dates;
    private List<Integer> nums;

    public DayDate(List<String> dates, List<Integer> nums) {
        this.dates = dates;
        this.nums = nums;
    }
}
