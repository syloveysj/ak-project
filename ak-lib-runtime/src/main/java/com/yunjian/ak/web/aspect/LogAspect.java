package com.yunjian.ak.web.aspect;

import com.yunjian.ak.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/11/14 23:41
 * @Version 1.0
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    //设置切入点：这里直接拦截被@RestController注解的类
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void pointcut() {
    }

    /**
     * 切面方法,记录日志
     *
     * @return
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //1、开始时间
        long beginTime = System.currentTimeMillis();

        //利用RequestContextHolder获取requst对象
        ServletRequestAttributes requestAttr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String uri = requestAttr.getRequest().getRequestURI();
        log.info("开始计时: {}  URI: {}", new Date(), uri);

        //访问目标方法的参数 可动态改变参数值
        Object[] args = joinPoint.getArgs();
        //方法名获取
        String methodName = joinPoint.getSignature().getName();
        log.info("请求方法：{}, 请求参数: {}", methodName, Arrays.toString(args));
        //可能在反向代理请求进来时，获取的IP存在不正确性
        log.info("请求ip：{}", IPUtil.getIpAddr(requestAttr.getRequest()));

        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("暂不支持非方法注解");
        }

        //调用实际方法
        Object object = joinPoint.proceed();
        MethodSignature methodSign = (MethodSignature) signature;
        //获取执行的方法
        Method method = methodSign.getMethod();

        //这个方法才是目标对象上有注解的方法
        //Method realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), method.getParameterTypes());
        Log logAnno = AnnotationUtils.getAnnotation(method, Log.class);
        //判断是否包含了 无需记录日志的方法
        if (logAnno != null && logAnno.ignore()) {
            return object;
        }
        if(logAnno != null) {
            log.info("log注解描述：{}", logAnno.desc());
        }
        long endTime = System.currentTimeMillis();
        log.info("结束计时: {},  URI: {},耗时：{}", new Date(), uri, endTime - beginTime);

        //模拟异常
        //System.out.println(1/0);

        return object;
    }

    /**
     * 指定拦截器规则；也可直接使用within(@org.springframework.web.bind.annotation.RestController *)
     * 这样简单点 可以通用
     *
     * @param e
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void afterThrowable(Throwable e) {
        //这里可以做个统一异常处理
        log.error("切面发生了异常：", e);

        //自定义一个异常 包装后排除
        //throw new AopException("xxx);
    }

}
