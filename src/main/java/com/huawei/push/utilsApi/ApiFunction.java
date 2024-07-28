package com.huawei.push.utilsApi;

@FunctionalInterface
public interface ApiFunction<F, T> {
    T apply(F var1);
}