package com.qf.servicrImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.CartMapper;
import com.qf.dao.GoodsMapper;
import com.qf.entity.Goods;
import com.qf.entity.ShopCart;
import com.qf.entity.User;
import com.qf.service.ICartService;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
   private GoodsMapper goodsMapper;
    @Autowired
    private IGoodsService goodsService;
    @Override
    public int mergeCart(User user,String cartToken) {
        //怎么合并
        //当登录时就把缓存中的加到用户中,然后清空redis中的
        //登录后跳到首页的时候
        //做一个判断,如果用户是登录状态,跳到首页就把购物车合并
        //把临时购物车的信息添加到用户的购物车中
        //1.获得临时购物车的信息
        if(cartToken!=null){
            Long size = redisTemplate.opsForList().size(cartToken);
            List<ShopCart> cartList = redisTemplate.opsForList().range(cartToken, 0, size);

            if(cartList==null){
                return 1;
            }
            //2.判断是否为空,不为空就添加到登录的用户中

                for (ShopCart cart : cartList) {
                    cart.setUid(user.getId());
                    cartMapper.insert(cart);
                }

            //清空缓存
            redisTemplate.delete(cartToken);
        }


        return 1;
    }

    /**
     * 添加购物车的方法
     * @param user
     * @param shopCart
     * @param cartToken
     * @return
     */
    @Override
    public int insertCart(User user, ShopCart shopCart, String cartToken) {
        //判断用户是否处于登录状态
        //登录就加入数据库

                    Goods goods = goodsService.queryGoodsById(shopCart.getGid());
                    BigDecimal gnumber = BigDecimal.valueOf(shopCart.getGnumber());
                    shopCart.setAllprice(goods.getGprice().multiply(gnumber));//小计
                    if (user!=null){
                        //添加要判断这个是加的同一个商品还是不同的商品
                        //同一个商品,加数量

                        shopCart.setUid(user.getId());
                        cartMapper.insert(shopCart);

                    }else{//未登录就加入redis中
                        redisTemplate.opsForList().leftPush(cartToken,shopCart);

                    }


        return 1;
    }

    /**
     *查询购物车信息
     * @param user
     * @param cartToken
     * @return
     */
    @Override
    public List<ShopCart> queryCart(User user, String cartToken) {
        List<ShopCart> cartList =null;
        if(user!=null){
            //用户登录,从数据库中取
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("uid",user.getId());
            cartList = cartMapper.selectList(queryWrapper);
      }else if(cartToken!=null){
            //从缓存中取看能不能取到
            Long size = redisTemplate.opsForList().size(cartToken);


             cartList = redisTemplate.opsForList().range(cartToken, 0, size);

        }
        //查出商品信息,图片,商品名称
        if(cartList!=null){//
            for (ShopCart shopCart : cartList) {
                Goods goods= goodsMapper.selectById(shopCart.getGid());
                shopCart.setGoods(goods);
            }
        }

        return cartList;
    }

    @Override
    public int deleteCartByUId(int uid) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("uid",uid);
       int result= cartMapper.delete(queryWrapper);
        return result;
    }

}
