package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.GoodsMapper;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import com.qf.service.ISearchService;
import com.qf.shop_service_goods.ConfigurationPriovide;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class GoodsServiceImpl implements IGoodsService{
    @Autowired
    private GoodsMapper goodsMapper;
    @Reference
    private ISearchService searchService;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public List<Goods> queryAll() {
        return goodsMapper.selectList(null);
    }

    @Override
    public int insert(Goods goods) {
        //添加商品
        int result= goodsMapper.insert(goods);
        //发送消息,第一个参数是交换机,第三个是传递的消息内容
        rabbitTemplate.convertAndSend(ConfigurationPriovide.FANTOUT_NAME,null,goods);

        //searchService.insertSearch(goods);
        return  result;
    }

    @Override
    public Goods queryGoodsById(int id) {
        Goods goods = goodsMapper.selectById(id);

        return goods;
    }
}
