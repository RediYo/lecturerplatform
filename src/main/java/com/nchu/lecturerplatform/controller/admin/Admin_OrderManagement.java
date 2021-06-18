package com.nchu.lecturerplatform.controller.admin;


import com.alipay.api.AlipayApiException;
import com.nchu.lecturerplatform.domain.Order;
import com.nchu.lecturerplatform.domain.StuCourse;
import com.nchu.lecturerplatform.repository.OrdersRepository;
import com.nchu.lecturerplatform.repository.StuCourseRepository;
import com.nchu.lecturerplatform.service.OrderService;
import com.nchu.lecturerplatform.service.PayService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;

@RequestMapping("/admin/order")
@Controller
public class Admin_OrderManagement {

    @Resource
    private OrdersRepository ordersRepository;
    @Resource
    private PayService payService;
    @Resource
    private StuCourseRepository stuCourseRepository;
    @Resource
    private OrderService orderService;

    @GetMapping("/list")
    public String order(Model model,@RequestParam(value = "keyword", defaultValue = "0") Long keyword, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,@RequestParam(value = "pageSize", defaultValue = "5") int pageSize){
        Page<Order> orderList=null;
        if (keyword == 0) {//查找全部
            orderList=orderService.getOrderListByPayStatus(pageNum,pageSize,1);
        }else {
            orderList=orderService.getOrderListById(pageNum,pageSize,keyword);
        }
        //List<Order> orderList=(List<Order>)ordersRepository.findAll();
        model.addAttribute("orderList",orderList);
        int money=0;
        if (ordersRepository.countMoney() != null) {
            money=ordersRepository.countMoney();
        }
        model.addAttribute("total",money);//成交额
        return "admin/admin_orderList";
    }

    @PostMapping("/search")
    public String searchByKeyword(String keyword) {//按关键字模糊查询
        return "redirect:/admin/order/list?keyword="+ keyword;
    }

    @GetMapping("/refund")
    public String refund(Long orderId) throws AlipayApiException {
        Order order=ordersRepository.findById(orderId).orElse(null);
        boolean refundState=payService.refund(order);
        if(refundState){//退款成功

            StuCourse stuCourse=stuCourseRepository.findByUsernameAndCourseId(order.getStudent().getUsername(),order.getCourse().getId());
            stuCourseRepository.delete(stuCourse);
            ordersRepository.delete(order);

        }
        return "redirect:/admin/order/list";
    }
}
