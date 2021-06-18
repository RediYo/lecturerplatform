package com.nchu.lecturerplatform.service;

import com.nchu.lecturerplatform.domain.Applicant;
import com.nchu.lecturerplatform.domain.Order;
import org.springframework.data.domain.Page;

public interface OrderService {

    Page<Order> getOrderListByPayStatus(int pageNum, int pageSize,int payStatus);
    Page<Order> getOrderListById(int pageNum, int pageSize,Long keyword);
}
