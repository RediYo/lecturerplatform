package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.SearchKeyRanking;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SearchKeyRankingRepository extends CrudRepository<SearchKeyRanking,String> {

    List<SearchKeyRanking> findTop8ByOrderBySearchNumDesc();
}
