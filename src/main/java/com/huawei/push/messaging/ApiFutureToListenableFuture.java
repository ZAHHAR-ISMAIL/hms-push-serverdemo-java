package com.huawei.push.messaging;


import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//@InternalApi
public class ApiFutureToListenableFuture<V> implements ListenableFuture<V> {
    private final ApiFuture<V> apiFuture;

    public ApiFutureToListenableFuture(ApiFuture<V> apiFuture) {
        this.apiFuture = apiFuture;
    }

    public void addListener(Runnable listener, Executor executor) {
        this.apiFuture.addListener(listener, executor);
    }

    public boolean cancel(boolean b) {
        return this.apiFuture.cancel(b);
    }

    public boolean isCancelled() {
        return this.apiFuture.isCancelled();
    }

    public boolean isDone() {
        return this.apiFuture.isDone();
    }

    public V get() throws InterruptedException, ExecutionException {
        return this.apiFuture.get();
    }

    public V get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.apiFuture.get(l, timeUnit);
    }
}
