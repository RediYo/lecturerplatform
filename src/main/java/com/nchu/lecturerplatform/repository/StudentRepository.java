package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student,String> {
    Student findStudentByCode(String code);
    void saveAndFlush(Student student);
}
