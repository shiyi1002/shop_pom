package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 搜索工程
 */
@Controller
@RequestMapping("/goods")
public class SearchController {
    @Reference
    private ISearchService searchService;
    @RequestMapping("/search")
    public String show(String keyword, Model model){
        List<Goods> goods = searchService.searchGoods(keyword);
        model.addAttribute("goods",goods);

        return "searchList";
    }
}
