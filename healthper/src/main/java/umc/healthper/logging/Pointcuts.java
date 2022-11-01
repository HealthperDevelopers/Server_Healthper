package umc.healthper.logging;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {
    @Pointcut("execution(* umc.healthper.api..*(..))")
    public void controllerPoint(){}

    @Pointcut("execution(* umc.healthper.service..*(..))")
    public void servicePoint(){}

    @Pointcut("execution(* umc.healthper.repository..*(..))")
    public void repositoryPoint(){}
}
