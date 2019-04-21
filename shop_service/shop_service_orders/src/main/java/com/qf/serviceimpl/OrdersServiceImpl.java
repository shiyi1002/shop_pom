package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.OrderDetilsMapper;
import com.qf.dao.OrdersMapper;
import com.qf.entity.*;
import com.qf.service.IAddressService;
import com.qf.service.ICartService;
import com.qf.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrdersServiceImpl implements IOrdersService{
    @Autowired
    private OrdersMapper ordersMapper;
    @Reference
    private ICartService cartService;
    @Reference
    private IAddressService addressService;
    @Autowired
    private OrderDetilsMapper orderDetilsMapper;
    @Override
    public List<Orders> queryList(int uid) {
        return   ordersMapper.queryList(uid);

    }

    //把购物车的信息加入到订单中
    @Override
    @Transactional
    public String insertOrders(User user, int aid) {
        //生成订单页面,要订单信息,和订单详情信息展示
        //1.通过用户获得它的购物车信息
        List<ShopCart> cartList = cartService.queryCart(user, null);
        BigDecimal total=BigDecimal.valueOf(0.0);
        for (ShopCart shopCart : cartList) {
            total=total.add(shopCart.getAllprice());
        }
        //2.要把购物车的信息添加到订单中
        Address address = addressService.queryById(aid);
        Orders orders=new Orders(0, UUID.randomUUID().toString(),address.getPerson(),address.getAddress(),address.getPhone(),address.getCode(),total,new Date(),0,user.getId(),null);

        ordersMapper.insert(orders);
        //3.添加订单详情

        for (ShopCart shopCart : cartList) {
            //添加一个订单详情,就加入数据库一个
            OrderDetils orderDetils=new OrderDetils(0,shopCart.getGid(),shopCart.getGoods().getGimage(),shopCart.getGoods().getGname(),shopCart.getGoods().getGprice(),shopCart.getGnumber(),orders.getId());
            //入库
            orderDetilsMapper.insert(orderDetils);
        }
        //清空购物车
        cartService.deleteCartByUId(user.getId());

        return orders.getOrderid();
    }

    @Override
    public Orders queryOrderById(String oid) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("orderid",oid);
        return  ordersMapper.selectOne(queryWrapper);

    }

    @Override
    public int updateOrders(Orders orders) {
        return ordersMapper.update(orders,null);
    }
}
