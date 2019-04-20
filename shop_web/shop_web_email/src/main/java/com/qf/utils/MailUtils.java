package com.qf.utils;

import com.qf.entity.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


import javax.mail.internet.MimeMessage;


//让spring管理这个类
@Component
public class MailUtils {
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 发送邮件的方法
     * @param email
     */
    public  void send(Email email){
        //创建一封邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //创建邮件包装对象
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);

        try {
            //邮件的发送方
            mimeMessageHelper.setFrom(from,"新浪官方");
            System.out.println("邮件的发送方"+from);

            //接收方
            mimeMessageHelper.setTo(email.getTo());
            //邮件内容
            mimeMessageHelper.setText(email.getContent(),true);
            mimeMessageHelper.setSubject(email.getSubject());
            mimeMessageHelper.setSentDate(email.getCreatetime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //发送邮件
       javaMailSender.send(mimeMessage);
        System.out.println("工具类中的邮件:"+mimeMessage);
    }
}
