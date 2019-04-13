package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Value("${server.ip}")
    private String url;
    @Reference
    private IGoodsService goodsService;
    @RequestMapping("/list")
    public String getList(Model model){
        List<Goods> goodsList = goodsService.queryAll();
        model.addAttribute("goodsList",goodsList);
        model.addAttribute("url",url);
        return "goodsList";
    }
        @RequestMapping("/insert")
        public String insert(Goods goods){
            goodsService.insert(goods);
            return "redirect:/goods/list";
        }


}
