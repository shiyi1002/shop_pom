package com.qf.controller;

import com.qf.entity.Goods;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/toPage")
public class PageController {

    @RequestMapping("/{toPage}")
    public String getPage(@PathVariable("toPage") String toPage){
        return toPage;
    }



}
