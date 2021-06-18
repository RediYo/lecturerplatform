package com.nchu.lecturerplatform.service;

import com.nchu.lecturerplatform.domain.Applicant;
import com.nchu.lecturerplatform.domain.Order;
import com.nchu.lecturerplatform.repository.OrdersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl implements OrderService{

    @Resource
    private OrdersRepository ordersRepository;

    @Override
    public Page<Order> getOrderListByPayStatus(int pageNum, int pageSize,int payStatus) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Order> orderList = ordersRepository.findAllByPayStatus(pageable,payStatus);
        return orderList;
    }

    @Override
    public Page<Order> getOrderListById(int pageNum, int pageSize, Long id) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Order> orderList = ordersRepository.findById(pageable,id);
        return orderList;
    }
}
