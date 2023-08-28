package com.ydl.sms.aspect;

import com.ydl.context.BaseContextHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 通过切面方式，自定义注解，实现实体基础数据的注入（创建者、创建时间、修改者、修改时间）
 */
@Component
@Aspect
@Slf4j
public class DefaultParamsAspect {
    @SneakyThrows
    @Before("@annotation(com.ydl.sms.annotation.DefaultParams)")
    public void beforeEvent(JoinPoint point) {

        // TODO 自动注入基础属性（创建者、创建时间、修改者、修改时间）

        Long userId = BaseContextHandler.getUserId();
        Object[] args = point.getArgs();
        for (Object arg : args) {
            Class<?> argClass = arg.getClass();
            Object id = null;
            Method method = getMethod(argClass, "getId", String.class);
            if (method!=null){
                id = method.invoke(arg);
            }
            //1.1 新增  创建人 创建时间 修改人 修改时间
            if (id == null){
                method = getMethod(argClass, "setCreateUser", String.class); //设置创建人的方法
                if (method != null){
                    method.invoke(arg,userId.toString());
                }
                method = getMethod(argClass, "setCreateTime", LocalDateTime.class);
                if (method != null){
                    method.invoke(arg,LocalDateTime.now());
                }
            }

            //1.2 修改  修改人 修改时间
            method = getMethod(argClass, "setUpdateUser", String.class);
            if (method != null){
                method.invoke(arg,userId.toString());
            }
            method = getMethod(argClass, "setUpdateTime", LocalDateTime.class);
            if (method != null){
                method.invoke(arg,LocalDateTime.now());
            }
        }
    }

    /**
     * 获得方法对象
     * @param classes
     * @param name 方法名
     * @param types 参数类型
     * @return
     */
    private Method getMethod(Class classes, String name, Class... types) {
        try {
            return classes.getMethod(name, types);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
