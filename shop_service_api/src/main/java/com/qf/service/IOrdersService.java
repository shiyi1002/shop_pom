package com.qf.service;

import com.qf.entity.Orders;
import com.qf.entity.User;
import org.springframework.core.annotation.Order;

import java.util.List;

public interface IOrdersService {
    List<Orders> queryList(int uid);
    String insertOrders(User user, int aid);

  Orders  queryOrderById(String oid);

    int updateOrders(Orders orders);
}
