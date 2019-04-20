package com.qf.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qf.entity.Email;
import com.qf.entity.User;
import com.qf.service.ICartService;
import com.qf.service.IUserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/sso")
public class SSOController {
    @Reference
    private IUserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Reference
    private ICartService cartService;
    @RequestMapping("/toRegister")
    public String toRegister(){
        return "register";
    }

    /**
     * 跳转到登录页面
     * @param returnUrl
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin(String returnUrl,Model model){

        model.addAttribute("returnUrl",returnUrl);
        return "login";
    }

    /**
     * 注册的方法
     * @param user
     * @return
     */
    @RequestMapping("/register")
    public String register(User user){
        int result = userService.insertUser(user);
        if(result<=0){
            return "register";
        }

        //通过邮件激活
        //激活邮件的内容,收件人,发件人,标题
        //怎么激活
        //点击去激活就会发送一条邮件,点击链接就激活
        //收件人:用户,发件人,怎么发,邮件内容:链接
        //怎么激活:点击链接就跳到controller更新状态,更新完后跳转到登录页面
        Email email=new Email();
        email.setSubject("激活用户");

        String  url="http://localhost:8084/sso/active?username="+user.getUsername();
        //邮箱内容.一串链接地址,可以直接点开跳转
        email.setContent("<a href='"+url+"'>"+url+"</a>");
        //收件人
        email.setTo(user.getEmail());
        email.setCreatetime(new Date());
        String emailToken=UUID.randomUUID().toString();
        //邮件有过期时间,根据这个令牌来判断这个邮件有没有过期
        redisTemplate.opsForValue().set("emailToken",emailToken);
        redisTemplate.expire("emailToken",5,TimeUnit.MINUTES);
        //把消息放进队列
        rabbitTemplate.convertAndSend("email_queue",email);
       return "login";

    }

    @RequestMapping("/login")
    public String login(String username, String password, Model model, String returnUrl, HttpServletResponse response,
                        @CookieValue(name = "cartToken",required = false) String cartToken){

        User user = userService.loginUser(username, password);
        System.out.println("获得购物车的令牌"+cartToken);
        //存在这个用户,登录成功就跳到登录的那个页面,不成功就跳到登录页面
        if(user==null){
            model.addAttribute("error",0);
           return "login";
        }else if(user.getStatus()==0){//未激活
            //跳转到登录页面
            //http://mail.qq.com,一般邮箱都是mail后面接@后面的后缀
            String email=user.getEmail();
            //获取邮箱的@对应的下标
            int index=email.indexOf("@");
            //从@后面一位开始截取它的后缀
            String emailUrl="http://mail."+email.substring(index+1);
            model.addAttribute("emailUrl",emailUrl);
            model.addAttribute("error",1);

            return "login";
        }

        //手动在地址栏输入登录页面的地址时会为空(也就是不是通过首页进去时)
        if(returnUrl==null||returnUrl.equals("")){
            returnUrl="http://localhost:8081/";//就默认让它跳转到首页
        }
        //生成uuid作为凭证
        String uuid= UUID.randomUUID().toString();
        //把用户信息存到缓存中
        redisTemplate.opsForValue().set(uuid,user);
        redisTemplate.expire(uuid,10, TimeUnit.DAYS);
        model.addAttribute("user",user);
        //把登录的生成的uuid存到cookie中,前台来取,redis是后台用来取得
        Cookie cookie=new Cookie("token",uuid);
        //设置过期时间
        cookie.setMaxAge(60*60*24*10);
        cookie.setPath("/");
        //写入cookie
        response.addCookie(cookie);
        //哪个登录页面进来就跳到对应的页面
        //登录成功就进行合并购物车的操作
        int result=cartService.mergeCart(user,cartToken);

        return "redirect:"+returnUrl;
    }

    /**
     * 验证是否登录
     * @return
     */
    @RequestMapping("/isLogin")
    //通过@CookieValue取出cookie中的值
    @ResponseBody
    public String isLogin(@CookieValue(name = "token",required = false) String token){

       //通过cookie中的uuid来在redis中取这个值对应的user对象看能不能取得到
       User user=null;
        if(token!=null){
           user= (User) redisTemplate.opsForValue().get(token);

       }
       //三元运算符判断是否为空
       return user==null?"isLogin(null)":"isLogin('" + JSON.toJSONString(user) + "')";
    }

    /**
     * 注销
     * @param token
     * @param response
     * @return
     */
    @RequestMapping("/logout")
    public String logout(@CookieValue(name = "token",required = false) String token,HttpServletResponse response){
        //清cookie和redis
        Cookie cookie=new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        redisTemplate.delete(token);
        return "login";
    }

    /**
     * 邮箱的激活
     * @return
     */
    @RequestMapping("/active")
    public String activUser(String username){
        //改变用户的状态为1
        int result = userService.activeUser(username);
        return "login";
    }


}
