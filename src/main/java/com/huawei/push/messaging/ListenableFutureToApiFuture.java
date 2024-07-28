package com.huawei.push.messaging;

import com.google.common.util.concurrent.ForwardingListenableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import com.huawei.push.utilsApi.ApiFuture;

//@InternalApi
public class ListenableFutureToApiFuture<V> extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V> implements ApiFuture<V> {
    public ListenableFutureToApiFuture(ListenableFuture<V> delegate) {
        super(delegate);
    }
}