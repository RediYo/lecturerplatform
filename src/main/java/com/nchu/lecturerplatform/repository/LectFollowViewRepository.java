package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.LectFollowView;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LectFollowViewRepository extends CrudRepository<LectFollowView,String> {
    List<LectFollowView> findTop5ByOrderByFollowNumDesc();
    List<LectFollowView> findTop100ByOrderByFollowNumDesc();
}
