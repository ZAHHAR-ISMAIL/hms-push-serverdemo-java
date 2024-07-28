package com.huawei.push.utilsApi;

public interface ApiFutureCallback<V> {
    void onFailure(Throwable var1);

    void onSuccess(V var1);
}

