package com.braindocs.common.history;

import com.braindocs.services.HistoryService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class HistoryStampHook {

    private final HistoryService historyService;

    @Pointcut("within(@com.braindocs.common.history.TrackHistory *)")
    public void beanAnnotatedWithTrackHistory() {}

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {}

    @Pointcut("publicMethod() && (beanAnnotatedWithTrackHistory() || @annotation(com.braindocs.common.history.TrackHistory)))")
    public void trackHistoryMarked() {}

    @AfterReturning(value = "trackHistoryMarked()")
    public void trackHistoryHook(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // don't track for get requests and for those marked by don't track
        if (method.isAnnotationPresent(DoNotTrackHistory.class) || method.isAnnotationPresent(GetMapping.class)) {
            return;
        }
        TrackHistory trackHistory = method.getAnnotation(TrackHistory.class);
        if (trackHistory == null) {
            trackHistory = (TrackHistory) signature.getDeclaringType().getAnnotation(TrackHistory.class);
        }
        if (trackHistory == null) {
            throw new IllegalStateException("something wrong with aop");
        }

        historyService.createHistoryStamp(trackHistory.operation());
    }
}
