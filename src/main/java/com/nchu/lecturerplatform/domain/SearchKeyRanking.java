package com.nchu.lecturerplatform.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class SearchKeyRanking {

    @Id
    private String searchKey;
    private Long searchNum;

    public SearchKeyRanking(String searchKey, Long searchNum) {
        this.searchKey = searchKey;
        this.searchNum = searchNum;
    }

    public SearchKeyRanking() {

    }
}
