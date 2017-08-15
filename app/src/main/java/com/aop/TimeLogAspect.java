package com.aop;


import android.os.Build;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;

import com.base.util.LogUtils;
import com.socks.library.KLog;
import com.utils.StringUtil;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.TimeUnit;

/**
 * 根据注解TimeLog自动添加打印方法耗代码，通过aop切片的方式在编译期间织入源代码中
 * 功能：自动打印方法的耗时
 */
@Aspect
public class TimeLogAspect {

//    @Pointcut("execution(@com.app.annotation.aspect.TimeLog * *(..))")//方法切入点
//    public void methodAnnotated() {
//    }
//
//    @Pointcut("execution(@com.app.annotation.aspect.TimeLog *.new(..))")//构造器切入点
//    public void constructorAnnotated() {
//    }
//
//    @Around("methodAnnotated() || constructorAnnotated()")//在连接点进行方法替换
//    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        LogUtils.showLog("TimeLog getDeclaringClass", methodSignature.getMethod().getDeclaringClass().getCanonicalName());
//        String className = methodSignature.getDeclaringType().getSimpleName();
//        String methodName = methodSignature.getName();
//        long startTime = System.nanoTime();
//        Object result = joinPoint.proceed();//执行原方法
//        StringBuilder keyBuilder = new StringBuilder();
//        keyBuilder.append(methodName + ":");
//        for (Object obj : joinPoint.getArgs()) {
//            if (obj instanceof String) keyBuilder.append((String) obj);
//            else if (obj instanceof Class) keyBuilder.append(((Class) obj).getSimpleName());
//        }
//        String key = keyBuilder.toString();
//        //LogUtils.showLog
//        KLog.json("TimeLog", (className + "." + key + joinPoint.getArgs().toString() + " --->:" + "[" + (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)) + "ms]"));// 打印时间差
//        return result;
//    }

    private static volatile boolean enabled = true;

    @Pointcut("within(@com.app.annotation.aspect.TimeLog *)")
    public void withinAnnotatedClass() {}

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {}

    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {}

    @Pointcut("execution(@com.app.annotation.aspect.TimeLog * *(..)) || methodInsideAnnotatedType()")
    public void method() {}

    @Pointcut("execution(@com.app.annotation.aspect.TimeLog *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {}

//    public static void setEnabled(boolean enabled) {
//        Hugo.enabled = enabled;
//    }

    @Around("method() || constructor()")
    public Object logAndExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        enterMethod(joinPoint);

        long startNanos = System.nanoTime();
        Object result = joinPoint.proceed();
        long stopNanos = System.nanoTime();
        long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);

        exitMethod(joinPoint, result, lengthMillis);

        return result;
    }

    private static void enterMethod(JoinPoint joinPoint) {
        if (!enabled) return;

        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        Class<?> cls = codeSignature.getDeclaringType();
        String className = cls.getSimpleName();
        String methodName = codeSignature.getName();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();

        StringBuilder builder = new StringBuilder("---> ") // "\u21E2 "
                                    .append(className)
                                    .append(".")
                                    .append(methodName).append('(');
        for (int i = 0; i < parameterValues.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(parameterNames[i]).append('=');
            builder.append(StringUtil.toString(parameterValues[i]));
        }
        builder.append(')');

        if (Looper.myLooper() != Looper.getMainLooper()) {
            builder.append(" [Thread:\"").append(Thread.currentThread().getName()).append("\"]");
        }

        final String str = builder.toString();

        KLog.json(asTag(cls), str);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final String section = str.substring(2);
            Trace.beginSection(section);
        }
    }

    private static void exitMethod(JoinPoint joinPoint, Object result, long lengthMillis) {
        if (!enabled) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.endSection();
        }

        Signature signature = joinPoint.getSignature();

        Class<?> cls = signature.getDeclaringType();
        String className = cls.getSimpleName();
        String methodName = signature.getName();
        boolean hasReturnType = signature instanceof MethodSignature
                && ((MethodSignature) signature).getReturnType() != void.class;

        StringBuilder builder = new StringBuilder("<--- ")//⇠
                .append(className)
                .append(".")
                .append(methodName)
                .append(" [")
                .append(lengthMillis)
                .append("ms]");

        if (hasReturnType) {
            builder.append(" = ");
            builder.append(StringUtil.toString(result));
        }

        KLog.json(asTag(cls), builder.toString());
    }

    private static String asTag(Class<?> cls) {
        if (cls.isAnonymousClass()) {
            return asTag(cls.getEnclosingClass());
        }
        return cls.getSimpleName();
    }
}