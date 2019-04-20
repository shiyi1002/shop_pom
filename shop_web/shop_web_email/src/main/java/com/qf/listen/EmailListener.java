package com.qf.listen;


import com.qf.entity.Email;
import com.qf.utils.MailUtils;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class EmailListener {
    @Autowired
    private MailUtils mailUtils;
    //监听到队列中有邮件,就发送邮件
    @RabbitListener(queues = "email_queue")
    public void emailHandler(Email email){
        try {
            mailUtils.send(email);
            System.out.println("监听" +email);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}