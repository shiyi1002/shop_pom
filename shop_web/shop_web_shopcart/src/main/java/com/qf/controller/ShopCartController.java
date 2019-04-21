package com.qf.controller;

import com.alibaba.fastjson.JSON;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.aspect.ISLogin;
import com.qf.entity.Address;
import com.qf.entity.ShopCart;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import com.qf.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class ShopCartController {
    @Reference
    private ICartService cartService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Reference
    private IAddressService addressService;
    /**
     * 加入购物车
     * @return
     */
    @RequestMapping("/addCart")
    @ISLogin
    public String addCart(User user, ShopCart shopCart, @CookieValue(name = "cartToken",required = false) String cartToken, HttpServletResponse response){
        //商品信息添加到购物车里面
        //做判断是登录还是未登录的状态
        if(cartToken==null){
            //如果没有令牌就创建一个购物车令牌
            cartToken= UUID.randomUUID().toString();
            Cookie cookie=new Cookie("cartToken",cartToken);
            cookie.setMaxAge(60*60*24*10);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        cartService.insertCart(user,shopCart,cartToken);
        //页面展示商品
            //点击首页和返回都是跳到

        return "addcart";
    }

    /**
     * 跳到购物车页面展示购物车的商品
     * @return
     */
    @RequestMapping("/cartList")
    @ISLogin
    public String cartList(@CookieValue(name = "cartToken") String cartToken,User user,  Model model){
        //查询购物车的商品
        List<ShopCart> cartList = cartService.queryCart(user, cartToken);
        //商品小计
            BigDecimal total=BigDecimal.valueOf(0.0);
        for (ShopCart shopCart : cartList) {
            total=total.add(shopCart.getAllprice());
        }
        model.addAttribute("total",total);
        model.addAttribute("cartList",cartList);
        //查询商品的信息
        return "cartList";
    }

    /**
     * 下拉展示购物车信息
     * @return
     */
    @RequestMapping("/showList")
    @ISLogin
    @ResponseBody
    public String showList(User user, @CookieValue(name = "cartToken",required = false) String cartToken){
        //查购物车信息
        //一种是用户登录的状态,通过uid查询数据库
        List<ShopCart> cartList=cartService.queryCart(user,cartToken);

        //一种是未登录的状态,查缓存redis
            return "cartShow("+ JSON.toJSONString(cartList)+")";

    }

    //跳转到订单页面
    @RequestMapping("/toOrder")
    @ISLogin(mustLogin = true)
    public String toOrder(User user,@CookieValue(name = "cartToken",required = false) String cartToken,Model model){
        //查购物车信息
        List<ShopCart> cartList=cartService.queryCart(user,cartToken);
        //总价
        BigDecimal total=BigDecimal.valueOf(0.0);
        for (ShopCart shopCart : cartList) {
            total=total.add(shopCart.getAllprice());
        }
        model.addAttribute("total",total);
        //查地址表
        if(user!=null){
            List<Address> addresses = addressService.queryAddress(user.getId());
            model.addAttribute("addresses",addresses);
        }

        model.addAttribute("cartList",cartList);

        return "ordersList";
    }


}
