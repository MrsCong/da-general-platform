package com.dgp.redis.aspect;

import cn.hutool.core.util.StrUtil;
import com.dgp.common.utils.EnvUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class ScheduleAspect {

    public static final int WAIT_TIME = 0;

    public static final int LEASE_TIME = 5;

    @Resource
    private RedissonClient redissonClient;

    private static final String REDISSON_LOCK_PREFIX = "dgp:redis:schedule:redisLock:";

    @Around("@annotation(com.dgp.redis.annotation.DistributedSchedule)")
    public Object around(ProceedingJoinPoint joinPoint)
            throws NoSuchMethodException, InterruptedException {
        Method method = getMethod(joinPoint);
        String lockName = getRedisKey(method);
        RLock rLock = redissonClient.getLock(lockName);
        Object result = null;
        if (rLock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.MINUTES)) {
            try {
                result = joinPoint.proceed();
            } catch (Throwable throwable) {
                log.error("execute schedule job error", throwable);
            } finally {
                if (rLock.isLocked()) {
                    rLock.unlock();
                }
            }
        }
        return result;
    }

    public String getRedisKey(Method method) {
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String redisKey = REDISSON_LOCK_PREFIX + className + StrUtil.COLON + methodName;
        redisKey = EnvUtil.getEnvRedis(redisKey);
        return redisKey;
    }

    public Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Signature sig = joinPoint.getSignature();
        MethodSignature msig;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        return target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
    }

}
