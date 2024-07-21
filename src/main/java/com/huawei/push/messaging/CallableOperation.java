package com.huawei.push.messaging;

import com.google.common.base.Preconditions;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.Callable;

public abstract class CallableOperation<T, V extends Exception> implements Callable<T> {
    public CallableOperation() {
    }

    protected abstract T execute() throws V;

    public final T call() throws V {
        return this.execute();
    }

    public final ApiFuture<T> callAsync(@NonNull HuaweiApp app) {
        Preconditions.checkNotNull(app);
        return ImplHuaweiTrampolines.submitCallable(app, this);
    }
}