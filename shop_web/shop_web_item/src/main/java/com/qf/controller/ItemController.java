package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/item")
public class ItemController {
    @Reference
    private IGoodsService goodsService;
    @Autowired
    private Configuration configuration;

    @RequestMapping("/detail")
    public String show(int id, HttpServletRequest request) throws Exception {
        Goods goods = goodsService.queryGoodsById(id);

        String gimage = goods.getGimage();
        String[] images=gimage.split("|");
        //获得模板对象
        Template template = configuration.getTemplate("itemlist.ftl");
        //准备数据
        Map<String,Object> map=new HashMap<>();
        map.put("goods",goods);
        //页面的根路径
        map.put("context",request.getContextPath());
        map.put("images",images);
        String path=this.getClass().getResource("/static/page").getPath()+goods.getId()+".html";
        template.process(map,new FileWriter(path));
        return "itemlist.ftl";
    }

  @RequestMapping("/hello")
    public String hello() {
        return "hello";

    }
}
