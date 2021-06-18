package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.UserStars;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserStarsRepository extends CrudRepository<UserStars,Long> {

    List<UserStars> findByCiFirstAndCiSecond(Long ciFirst,Long ciSecond);
}
