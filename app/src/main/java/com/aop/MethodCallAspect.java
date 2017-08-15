package com.aop;

import android.text.TextUtils;
import android.util.Log;

import com.base.util.LogUtils;
import com.socks.library.KLog;
import com.utils.StringUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by haozhong on 2017/8/14.
 * 功能：跟踪方法的调用顺序
 * log示列: Call --> MethodCallAspect.setEnable(boolean enable)
 * bug：log无法跳转到指定的方法,有偏差
 */
@Aspect
public class MethodCallAspect {

    private static volatile  boolean enable = true;

    public static void setEnable(boolean enable) {
        MethodCallAspect.enable = enable;
    }

    //  "within(com.base..*)" +
    //匹配com.base包极其子包下的所有方法
    @Pointcut("execution(* com..*.*(..))")//方法切入点() // && execution(!com.base.util.LogUtils * *(..))
    public void methodAnnotated() {
    }

    @Around("methodAnnotated()")//在连接点进行方法替换
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        if(enable) {
            CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

            Class<?> cls = codeSignature.getDeclaringType();
            String className = cls.getSimpleName();
            String methodName = codeSignature.getName();
            String[] parameterNames = codeSignature.getParameterNames();
            Class[] parameterTypes = codeSignature.getParameterTypes();

            StringBuilder builder = new StringBuilder("║ Call --> ") // "\u21E2 "
                    .append(className)
                    .append(".")
                    .append(methodName)
                    .append('(');
            int length = parameterNames.length;
            for (int i = 0; i < length; i++) {
                if (i > 0) builder.append(", ");
                builder.append(parameterTypes[i].getSimpleName())
                        .append(" ")
                        .append(parameterNames[i]);

            }
            builder.append(')');

            String tag = "call";
            String msg = builder.toString();
            StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
            int currentIndex = -1;
            for (int i = 0; i < stackTraceElement.length; i++) {
                if (stackTraceElement[i].getMethodName().compareTo("aroundJoinPoint") == 0) {
                    currentIndex = i + 1;
                    break;
                }
            }
            if (currentIndex >= 0) {
                String fullClassName = stackTraceElement[currentIndex].getClassName();
                String _className = fullClassName.substring(fullClassName
                        .lastIndexOf(".") + 1).split("\\$")[0];
                String _methodName = stackTraceElement[currentIndex].getMethodName();
                String lineNumber = String
                        .valueOf(stackTraceElement[currentIndex].getLineNumber()-1);

                Log.i(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");

                Log.i(tag,new StringBuilder("║ [ ")
                        .append("(")
                        .append(_className)
                        .append(".java:")
                        .append(lineNumber)
                        .append(")")
                        .append(" ==> ")
                        .append( _className)
                        .append(".")
                        .append(_methodName)
                        .append(" ] \n")
                        .toString()
                        + msg);

                Log.i(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");

            } else {
                Log.i(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");

                Log.i(tag, builder.toString());

                Log.i(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
            }
        }
        return joinPoint.proceed();
    }
}
