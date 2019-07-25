package com.polan.logback.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * <p> controller 接口请求参数日志打印</p>
 *
 * @author panliyong  2019-07-20 13:43
 */
@Aspect
@Component
@Slf4j
public class RequestAopLogAspect {
    //
    // @Autowired
    // private PrincipalUtils principalUtils;

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 打印请求
        // JwtPrincipal jwtPrincipal = principalUtils.getJwtPrincipal(request);
        // Integer id = null;
        // String name = null;
        // if (Objects.nonNull(jwtPrincipal)) {
        //     id = jwtPrincipal.getId();
        //     name = jwtPrincipal.getName();
        // }

        String method = request.getMethod();
        StringBuilder logStr = new StringBuilder("【webLog】method=" + method + " URL=" + request.getRequestURI() + " consumed={consuming}ms");
        // StringBuilder logStr = new StringBuilder("【webLog】method=" + method + " URL=" + request.getRequestURI() + " consumed={consuming}ms currentUserId=" + id + " currentUserName=" + name);

        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = point.getArgs();
        for (int index = 0; index < args.length; index++) {
            Class<?> aClass = args[index].getClass();
            if ("BeanPropertyBindingResult".equals(aClass.getSimpleName())) {
                BeanPropertyBindingResult bindingResult = (BeanPropertyBindingResult) args[index];
                int errorCount = bindingResult.getErrorCount();
                String errMessage = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
                String bindMsg;
                if (errorCount > 0) {
                    bindMsg = "errorCount:" + errorCount + " errorMsg:[" + errMessage + "]";
                    logStr.append("\n").append(parameterNames[index]).append("=").append(bindMsg);
                }
                continue;
            }
            if (!("RequestWrapper".equals(aClass.getSimpleName()) || "FirewalledResponse".equals(aClass.getSimpleName()))) {
                logStr.append("\n").append(parameterNames[index]).append("=").append(args[index]);
            }
        }

        //执行方法
        Object result = point.proceed();

        // 执行耗时
        long consuming = System.currentTimeMillis() - startTime;
        logStr = new StringBuilder(logStr.toString().replace("{consuming}", consuming + ""));
        log.info(logStr.toString());
        return result;
    }

    @Pointcut(value = "execution(public * com.polan.logback.demo..*.*(..))")
    public void webLog() {
    }
}
