package org.lowell.apps.common.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.common.exception.ErrorCode;
import org.lowell.apps.common.util.CustomSpringELParser;
import org.lowell.apps.common.lock.exception.LockError;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(1)
public class DistributedLockAspect {
    private static final String LOCK_PREFIX = "lock:";
    private final LockManager lockManager;

    @Around("@annotation(org.lowell.apps.common.lock.DistributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock annotation = method.getAnnotation(DistributedLock.class);

        String lockKey = LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(),
                                                                            joinPoint.getArgs(),
                                                                            annotation.lockKey());
        boolean locked = false;
        try {
            locked = lockManager.tryLock(lockKey,
                                         annotation.waitTime(),
                                         annotation.leaseTime(),
                                         annotation.timeUnit());
            if (!locked) {
                throw new DomainException(LockError.NOT_ACQUIRE_LOCK, DomainException.createPayload(lockKey));
            }
            return joinPoint.proceed();
        } catch (Exception e) {
            log.error("Failed to acquire lock for key: {}", lockKey, e);
            if (e instanceof DomainException) {
                throw e;
            } else {
                throw DomainException.create(ErrorCode.INTERNAL_SERVER);
            }
        } finally {
            try {
                if (locked) {
                    lockManager.unlock(lockKey);
                }
            } catch (Exception e) {
                log.debug("lock is already released for lockKey[{}]", lockKey);
            }
        }
    }
     
}
