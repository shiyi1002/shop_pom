package com.qf.aspect;

import java.lang.annotation.*;

@Documented
@Retention(value = RetentionPolicy.RUNTIME)//运行时有效
@Target(value = ElementType.METHOD)
public @interface ISLogin {
    boolean  mustLogin() default false;//是否加这个注解必须登录才执行对应的增强
}
