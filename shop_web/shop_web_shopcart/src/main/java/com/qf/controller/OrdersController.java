package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.aspect.ISLogin;
import com.qf.entity.Address;
import com.qf.entity.Orders;
import com.qf.entity.ShopCart;
import com.qf.entity.User;

import com.qf.service.ICartService;
import com.qf.service.IOrdersService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/orders")
public class OrdersController {
    @Reference
    private IOrdersService ordersService;

    @Reference
    private ICartService cartService;
    /**
     * 生成订单页面
     * @return
     */
    @RequestMapping("/showOrder")
    @ISLogin(mustLogin = true)
    @ResponseBody
    public String showOrder(User user, int aid){
        return  ordersService.insertOrders(user,aid);


    }

    /**
     * 展示我的订单
     * @return
     */
    @RequestMapping("/myOrders")
    @ISLogin(mustLogin = true)
    public String myOrders(User user,Model model){
        //1.查询我的订单
        List<Orders> orderList = ordersService.queryList(user.getId());
        //2.查询我的订单详情
        model.addAttribute("orderList",orderList);
        return "OrderPay";
    }

}
