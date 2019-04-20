package com.qf.aspect;

import com.qf.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Arrays;

//用来判断用户是否登录
@Aspect
public class LoginAspect {
    @Autowired
    private RedisTemplate redisTemplate;

    @Around("@annotation(ISLogin)")//对加了这个注解的方法,加增强
    public Object isLogin(ProceedingJoinPoint proceedingJoinPoint) {

        try {
            //首先从cookie中获取用户的令牌
           ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            String uuid = null;
            Cookie[] cookies = request.getCookies();
            if(cookies!=null){
                for (Cookie cookie : cookies) {
                    //TODO
                   // System.out.println("没有登录凭证");

                    if (cookie.getName().equals("token")) { //
                        uuid = cookie.getValue();

                        break;
                    }
                }
               // System.out.println("获得cookie中的凭证：" + uuid);
            }


            User user = null;
            if(uuid != null){
                //说明有凭证，但是不一定代表登录，通过凭证去查询redis

                //2、查询redis
                user = (User) redisTemplate.opsForValue().get(uuid);
            }
            System.out.println("获得redis中的用户信息：" + user);
            if (user == null) {
                //未登录
                //获取加增强的方法的返回值类型
                //设置的true还是false,如果是true就代表必须登录才能加入购物车
                MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
                Method method = signature.getMethod();

                //获取注解@ISLogin
                ISLogin annotation = method.getAnnotation(ISLogin.class);

                //通过注解获取调用它这个里面的mustlogin方法的返回值类型boolean
                boolean flag = annotation.mustLogin();

                    if (flag) {//如果是true.则必须登录.强制跳转到登录页面

                        //获得当前这个请求浏览的地址,然后传过去,登录后直接跳转
                        String url = request.getRequestURL().toString();
                        System.out.println("url:" + url);
                        //获得?后面的参数字符串
                        String queryString = request.getQueryString();
                       // System.out.println("参数字符串" + queryString);
                        String returnUrl = url + "?" + queryString;
                        returnUrl = URLEncoder.encode(returnUrl, "utf-8");
                        return "http://localhost:8084/sso/toLogin?returnUrl=" + returnUrl;
                    }

            }

            //已经登录
            //获得被加增强方法的参数列表,把user设置进去
            Object[] args = proceedingJoinPoint.getArgs();


                //遍历这些参数,找到user类的
                for (int i = 0; i < args.length; i++) {
                    //args[i]!=null&&args[i].getClass()==User.class
                    //TODO
                    if (args[i]!=null&&args[i].getClass()==User.class) {
                        //把user给它设置进去
                        args[i] = user;
                        break;
                    }
                }

               // System.out.println("获得当前的新的参数列表：" + Arrays.toString(args));

               // System.out.println("执行增强前");
                //执行目标方法addcart
                Object result = proceedingJoinPoint.proceed(args);

                //System.out.println("执行增强后");
                return result;






        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;
    }
}