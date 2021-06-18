package com.nchu.lecturerplatform.util;

import com.nchu.lecturerplatform.domain.Admin;
import com.nchu.lecturerplatform.domain.Lecturer;
import com.nchu.lecturerplatform.domain.Student;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegistrationForm {

    private String username;
    private String password;

    public Student toStudent(PasswordEncoder passwordEncoder){
        //昵称默认为用户名
        return new Student(username,username,null,null,passwordEncoder.encode(password),1,"/images/default-pic.png");
    }

    public Lecturer toLecturer(PasswordEncoder passwordEncoder){
        //昵称默认为用户名
        return new Lecturer(username,username,null,null,passwordEncoder.encode(password),1,"/images/default-pic.png");
    }

    public Admin toAdmin(PasswordEncoder passwordEncoder){
        //昵称默认为用户名
        return new Admin(username,passwordEncoder.encode(password),1);
    }
}
