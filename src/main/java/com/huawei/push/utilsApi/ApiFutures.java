package com.huawei.push.utilsApi;


import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.huawei.push.messaging.ListenableFutureToApiFuture;

import java.util.List;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public final class ApiFutures {
    private ApiFutures() {
    }

    /** @deprecated */
    @Deprecated
    public static <V> void addCallback(ApiFuture<V> future, ApiFutureCallback<? super V> callback) {
        addCallback(future, callback, MoreExecutors.directExecutor());
    }

    public static <V> void addCallback(ApiFuture<V> future, final ApiFutureCallback<? super V> callback, Executor executor) {
        Futures.addCallback(listenableFutureForApiFuture(future), new FutureCallback<V>() {
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }

            public void onSuccess(V v) {
                callback.onSuccess(v);
            }
        }, executor);
    }

    /** @deprecated */
    @Deprecated
    public static <V, X extends Throwable> ApiFuture<V> catching(ApiFuture<? extends V> input, Class<X> exceptionType, ApiFunction<? super X, ? extends V> callback) {
        return catching(input, exceptionType, callback, MoreExecutors.directExecutor());
    }

    public static <V, X extends Throwable> ApiFuture<V> catching(ApiFuture<? extends V> input, Class<X> exceptionType, ApiFunction<? super X, ? extends V> callback, Executor executor) {
        ListenableFuture<V> catchingFuture = Futures.catching(listenableFutureForApiFuture(input), exceptionType, new ApiFunctionToGuavaFunction(callback), executor);
        return new ListenableFutureToApiFuture(catchingFuture);
    }

    public static <V, X extends Throwable> ApiFuture<V> catchingAsync(ApiFuture<V> input, Class<X> exceptionType, final ApiAsyncFunction<? super X, V> callback, Executor executor) {
        ListenableFuture<V> catchingFuture = Futures.catchingAsync(listenableFutureForApiFuture(input), exceptionType, new AsyncFunction<X, V>() {
            public ListenableFuture<V> apply(X exception) throws Exception {
                ApiFuture<V> result = callback.apply(exception);
                return ApiFutures.listenableFutureForApiFuture(result);
            }
        }, executor);
        return new ListenableFutureToApiFuture(catchingFuture);
    }

    public static <V> ApiFuture<V> immediateFuture(V value) {
        return new ListenableFutureToApiFuture(Futures.immediateFuture(value));
    }

    public static <V> ApiFuture<V> immediateFailedFuture(Throwable throwable) {
        return new ListenableFutureToApiFuture(Futures.immediateFailedFuture(throwable));
    }

    public static <V> ApiFuture<V> immediateCancelledFuture() {
        return new ListenableFutureToApiFuture(Futures.immediateCancelledFuture());
    }

    /** @deprecated */
    @Deprecated
    public static <V, X> ApiFuture<X> transform(ApiFuture<? extends V> input, ApiFunction<? super V, ? extends X> function) {
        return transform(input, function, MoreExecutors.directExecutor());
    }

    public static <V, X> ApiFuture<X> transform(ApiFuture<? extends V> input, ApiFunction<? super V, ? extends X> function, Executor executor) {
        return new ListenableFutureToApiFuture(Futures.transform(listenableFutureForApiFuture(input), new ApiFunctionToGuavaFunction(function), executor));
    }

    public static <V> ApiFuture<List<V>> allAsList(Iterable<? extends ApiFuture<? extends V>> futures) {
        return new ListenableFutureToApiFuture(Futures.allAsList(Iterables.transform(futures, new Function<ApiFuture<? extends V>, ListenableFuture<? extends V>>() {
            public ListenableFuture<? extends V> apply(ApiFuture<? extends V> apiFuture) {
                return ApiFutures.listenableFutureForApiFuture(apiFuture);
            }
        })));
    }

    //@BetaApi
    public static <V> ApiFuture<List<V>> successfulAsList(Iterable<? extends ApiFuture<? extends V>> futures) {
        return new ListenableFutureToApiFuture(Futures.successfulAsList(Iterables.transform(futures, new Function<ApiFuture<? extends V>, ListenableFuture<? extends V>>() {
            public ListenableFuture<? extends V> apply(ApiFuture<? extends V> apiFuture) {
                return ApiFutures.listenableFutureForApiFuture(apiFuture);
            }
        })));
    }

    /** @deprecated */
    @Deprecated
    public static <I, O> ApiFuture<O> transformAsync(ApiFuture<I> input, ApiAsyncFunction<I, O> function) {
        return transformAsync(input, function, MoreExecutors.directExecutor());
    }

    public static <I, O> ApiFuture<O> transformAsync(ApiFuture<I> input, final ApiAsyncFunction<I, O> function, Executor executor) {
        ListenableFuture<I> listenableInput = listenableFutureForApiFuture(input);
        ListenableFuture<O> listenableOutput = Futures.transformAsync(listenableInput, new AsyncFunction<I, O>() {
            public ListenableFuture<O> apply(I input) throws Exception {
                return ApiFutures.listenableFutureForApiFuture(function.apply(input));
            }
        }, executor);
        return new ListenableFutureToApiFuture(listenableOutput);
    }

    private static <V> ListenableFuture<V> listenableFutureForApiFuture(ApiFuture<V> apiFuture) {
        Object listenableFuture;
        if (apiFuture instanceof AbstractApiFuture) {
            listenableFuture = ((AbstractApiFuture)apiFuture).getInternalListenableFuture();
        } else {
            listenableFuture = new ApiFutureToListenableFuture(apiFuture);
        }

        return (ListenableFuture)listenableFuture;
    }

    private static class ApiFunctionToGuavaFunction<X, V> implements Function<X, V> {
        private ApiFunction<? super X, ? extends V> f;

        public ApiFunctionToGuavaFunction(ApiFunction<? super X, ? extends V> f) {
            this.f = f;
        }

        @Nullable
        public V apply(@Nullable X input) {
            return this.f.apply(input);
        }
    }
}

