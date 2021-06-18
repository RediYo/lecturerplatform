package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.CategoryWeight;
import org.springframework.data.repository.CrudRepository;

public interface CategoryWeightRepository extends CrudRepository<CategoryWeight,Long> {

    CategoryWeight findCategoryWeightByUsernameAndCategoryId(String username,long categoryId);
    CategoryWeight findTopByUsernameOrderByWeightDesc(String username);
}
