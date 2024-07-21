package com.huawei.push.messaging;

@FunctionalInterface
public interface ApiFunction<F, T> {
    T apply(F var1);
}