package com.qf.listener;

import com.qf.entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ItemListener {
    @Autowired
    private Configuration configuration;

    //生成一个静态页面
    @RabbitListener(queues = "good_queue2")
    public void getMsg(Goods goods){
        String gimage = goods.getGimage();
        String[] images=gimage.split("\\|");
        for (String image : images) {
            System.out.println(image);
        }
        //获得模板对象
        Template template = null;
        try {
            template = configuration.getTemplate("itemlist.ftl");
            //准备数据
            Map<String,Object> map=new HashMap<>();
            map.put("goods",goods);
            //页面的根路径
           // map.put("context",request.getContextPath());
            //这个map相当于之前的model,把数据带到前台去
            map.put("images",images);
            String path=this.getClass().getResource("/static/page/").getPath()+goods.getId()+".html";
            template.process(map,new FileWriter(path));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
