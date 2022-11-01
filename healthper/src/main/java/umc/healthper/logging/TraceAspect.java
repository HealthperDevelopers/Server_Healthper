package umc.healthper.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import umc.healthper.logging.log.LogTrace;
import umc.healthper.logging.log.trace.TraceStatus;

@Aspect
@RequiredArgsConstructor
public class TraceAspect {
    private final LogTrace logTrace;

    @Around("umc.healthper.logging.Pointcuts.controllerPoint() ||" +
            "umc.healthper.logging.Pointcuts.servicePoint()|| " +
            "umc.healthper.logging.Pointcuts.repositoryPoint()"
    )
    public Object execute(ProceedingJoinPoint joinPoint)throws Throwable{
        //내부 로직이 어드바이스
        TraceStatus status = null;
        try {
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);

            //로직 호출
            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        }catch (Exception e){
            logTrace.exception(status, e);
            throw e;
        }
    }//어드바이저 탄생
}
