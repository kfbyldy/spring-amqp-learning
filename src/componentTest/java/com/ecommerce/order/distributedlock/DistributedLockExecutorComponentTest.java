package com.ecommerce.order.distributedlock;

import com.ecommerce.order.BaseComponentTest;
import com.ecommerce.order.common.distributedlock.DistributedLockExecutor;
import com.ecommerce.order.common.distributedlock.LockAlreadyOccupiedException;
import net.javacrumbs.shedlock.core.LockConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ecommerce.order.common.exception.DefaultErrorCode.LOCK_OCCUPIED;
import static java.time.Instant.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DistributedLockExecutorComponentTest extends BaseComponentTest {

    @Autowired
    private DistributedLockExecutor executor;

    @Test
    public void should_lock() {
        executeWithLock();
        LockAlreadyOccupiedException exception = assertThrows(LockAlreadyOccupiedException.class,
                this::executeWithLock);
        assertEquals(LOCK_OCCUPIED.name(), exception.getError().getCode());
    }

    private String executeWithLock() {
        return executor.executeWithLock(() -> "hello world.",
                new LockConfiguration("componentTest", now().plusSeconds(10), now().plusSeconds(5)));
    }

}