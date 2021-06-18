package com.nchu.lecturerplatform.service;

import com.alipay.api.AlipayApiException;
import com.nchu.lecturerplatform.domain.Order;

public interface PayService {


    String pay(Order order) throws AlipayApiException;//课程 id
    String bills() throws AlipayApiException;
    Boolean queryBillState(Order order) throws AlipayApiException;
    Boolean refund(Order order) throws AlipayApiException;
}
