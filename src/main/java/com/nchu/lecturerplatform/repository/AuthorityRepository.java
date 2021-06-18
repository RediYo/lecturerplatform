package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.Role;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Role,Long> {

    Role findByAuthority(String authority);
}