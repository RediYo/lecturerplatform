package com.nchu.lecturerplatform.service;

import com.nchu.lecturerplatform.domain.Student;

public interface StudentService {
    /**
     * 根据激活码code查询用户，之后再进行修改状态
     */
    Student checkCode(String code);

    /**
     * 激活账户，修改用户状态为“1”
     */
    void updateStudentStatus(Student student);

}
