package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.Applicant;
import com.nchu.lecturerplatform.domain.Course;
import com.nchu.lecturerplatform.domain.Order;
import com.nchu.lecturerplatform.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Order,Long> {

    List<Order> findAllByStudent(Student student);
    List<Order> findAllByStudentAndPayStatus(Student student,int payStatus);
    Order findByStudentAndCourse(Student student, Course course);
    @Query(value="select o.* from Orders o,lecturer l,course c where l.username=c.username and c.id=o.course_id and c.username=?1 and o.pay_status=1 order by o.create_time DESC ;", nativeQuery = true)
    List<Order> findByUsernameOrderByCreateTimeDesc(String username);
    @Query(value="SELECT sum(c.money) from Orders o,course c where o.course_id=c.id;", nativeQuery = true)
    Integer countMoney();
    Page<Order> findById(Pageable pageable,Long id);
    Page<Order> findAllByPayStatus(Pageable pageable,int payStatus);
}
