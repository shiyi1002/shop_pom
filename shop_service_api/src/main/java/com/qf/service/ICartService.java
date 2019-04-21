package com.qf.service;

import com.qf.entity.ShopCart;
import com.qf.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ICartService {


    int mergeCart(User user,String cartToken);//合并购物车

    int insertCart(User user, ShopCart shopCart, String cartToken);

    List<ShopCart> queryCart(User user, String cartToken);
    int deleteCartByUId(int uid);
}
