package com.huawei.push.messaging;

import com.huawei.push.reponse.SendResponse;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;


public interface BatchResponse {

    @NonNull
    List<SendResponse> getResponses();

    int getSuccessCount();

    int getFailureCount();
}