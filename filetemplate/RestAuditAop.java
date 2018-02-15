package ${PACKAGE_NAME};


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class ${NAME} {


    @Before("execution(public * com.kish.*.services.*Contoller.*(..))")
    public void logBeforeCall(JoinPoint jp){
      log.info("before calling the functions {}", jp.getSignature()) ;
      log.info("Args {} ", Arrays.toString(jp.getArgs()));

    }


    @AfterReturning(value = "execution(public * com.kish.*.services.*Contoller.*(..))", returning = "returnedValue")
    public void logBeforeCall(JoinPoint jp,Object returnedValue){
      log.info("after calling the functions {}", jp.getSignature()) ;
      log.info("response returned  {} ", returnedValue);

    }

    @AfterThrowing(value = "execution(public * com.kish.*.services.*Contoller.*(..))", throwing = "e")
    public void logBeforeCall(JoinPoint jp,Exception e){
      log.info("before calling the functions {}", jp.getSignature()) ;
      log.info("Exception returned  {} ", e.getCause());

    }

    //Additional methods for getting

    private HttpServletRequest getHttpServletRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }


    private HttpServletResponse getHttpServletResponse(){
        return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getResponse();
    }
}
