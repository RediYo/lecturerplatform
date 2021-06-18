package com.nchu.lecturerplatform.util;

import lombok.Data;

import java.util.List;

@Data
public class CategoryData {//用于管理员数据图表

    private List<String> categories;
    private List<Long> nums;

    public CategoryData(List<String> categories, List<Long> nums) {
        this.categories = categories;
        this.nums = nums;
    }
}
