package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.Lecturer;
import com.nchu.lecturerplatform.domain.Student;
import org.springframework.data.repository.CrudRepository;

public interface LecturerRepository extends CrudRepository<Lecturer,String> {
    Lecturer findByCode(String code);
    void saveAndFlush(Lecturer lecturer);

}
