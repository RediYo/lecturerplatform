package com.nchu.lecturerplatform.controller.teacher;


import com.nchu.lecturerplatform.domain.Lecturer;
import com.nchu.lecturerplatform.domain.Order;
import com.nchu.lecturerplatform.repository.LecturerRepository;
import com.nchu.lecturerplatform.repository.OrdersRepository;
import org.bouncycastle.math.raw.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/teacher/account")
public class Teach_AccountController {

    @Resource
    private LecturerRepository lecturerRepository;
    @Resource
    private OrdersRepository ordersRepository;

    @GetMapping("/view")
    public String view(Principal principal, Model model){
        Lecturer lecturer=lecturerRepository.findById(principal.getName()).orElse(null);
        model.addAttribute("lecturer",lecturer);
        List<Order> orderList=ordersRepository.findByUsernameOrderByCreateTimeDesc(principal.getName());
        model.addAttribute("orderList",orderList);
        return "/teacher/teach_myAccount";
    }
}
