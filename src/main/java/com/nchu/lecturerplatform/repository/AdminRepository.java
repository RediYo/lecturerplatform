package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.Admin;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin,String> {
}
