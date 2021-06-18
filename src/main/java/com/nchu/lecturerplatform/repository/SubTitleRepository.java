package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.SubTitle;
import com.nchu.lecturerplatform.domain.TopTitle;
import org.springframework.data.repository.CrudRepository;

public interface SubTitleRepository extends CrudRepository<SubTitle,Long> {

    SubTitle findByTopTitleAndTitle(TopTitle topTitle,String title);
    void deleteByTitleAndTopTitle(String title, TopTitle topTitle);
}
