package com.nchu.lecturerplatform.service;

import com.nchu.lecturerplatform.domain.Student;
import com.nchu.lecturerplatform.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentRepository studentRepository;
    /**
     * 注入邮件接口
     */
    @Resource
    private MailService mailService;

    @Override
    public Student checkCode(String code) {
        return studentRepository.findStudentByCode(code);
    }

    /**
     * 激活账户，修改用户状态
     */
    @Override
    public void updateStudentStatus(Student student) {
        studentRepository.saveAndFlush(student);
    }

}
