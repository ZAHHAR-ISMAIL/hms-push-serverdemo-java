package com.huawei.push.utilsApi;

@FunctionalInterface
public interface ApiAsyncFunction<I, O> {
    ApiFuture<O> apply(I var1) throws Exception;
}