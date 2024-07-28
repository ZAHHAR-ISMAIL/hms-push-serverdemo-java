package com.huawei.push.utilsApi;

import com.google.common.util.concurrent.ForwardingListenableFuture;
import com.google.common.util.concurrent.ListenableFuture;

public class ListenableFuture2ApiFuture<V> extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V> implements ApiFuture<V> {
    public ListenableFuture2ApiFuture(ListenableFuture<V> delegate) {
        super(delegate);
    }
}