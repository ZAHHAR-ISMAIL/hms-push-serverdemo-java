package com.huawei.push.messaging;

public interface ApiFutureCallback<V> {
    void onFailure(Throwable var1);

    void onSuccess(V var1);
}

