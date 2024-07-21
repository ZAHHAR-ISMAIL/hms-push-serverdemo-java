package com.huawei.push.messaging;


import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.ListenableFuture;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractApiFuture<V> implements ApiFuture<V> {
    private final AbstractApiFuture<V>.InternalSettableFuture impl = new InternalSettableFuture();

    public AbstractApiFuture() {
    }

    public void addListener(Runnable listener, Executor executor) {
        this.impl.addListener(listener, executor);
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return this.impl.cancel(mayInterruptIfRunning);
    }

    public V get() throws InterruptedException, ExecutionException {
        return this.impl.get();
    }

    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.impl.get(timeout, unit);
    }

    public boolean isCancelled() {
        return this.impl.isCancelled();
    }

    public boolean isDone() {
        return this.impl.isDone();
    }

    protected boolean set(V value) {
        return this.impl.set(value);
    }

    protected boolean setException(Throwable throwable) {
        return this.impl.setException(throwable);
    }

    protected void interruptTask() {
    }

    ListenableFuture<V> getInternalListenableFuture() {
        return this.impl;
    }

    private class InternalSettableFuture extends AbstractFuture<V> {
        private InternalSettableFuture() {
        }

        protected boolean set(@Nullable V value) {
            return super.set(value);
        }

        protected boolean setException(Throwable throwable) {
            return super.setException(throwable);
        }

        protected void interruptTask() {
            AbstractApiFuture.this.interruptTask();
        }
    }
}


