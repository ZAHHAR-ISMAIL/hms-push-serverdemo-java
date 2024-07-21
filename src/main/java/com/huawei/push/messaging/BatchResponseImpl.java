package com.huawei.push.messaging;

import com.google.common.collect.ImmutableList;
import com.huawei.push.reponse.SendResponse;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

class BatchResponseImpl implements BatchResponse {

    private final List<SendResponse> responses;
    private final int successCount;

    BatchResponseImpl(List<SendResponse> responses) {
        this.responses = ImmutableList.copyOf(responses);
        int successCount = 0;
        for (SendResponse response : this.responses) {
            if (response.isSuccessful()) {
                successCount++;
            }
        }
        this.successCount = successCount;
    }

    @NonNull
    public List<SendResponse> getResponses() {
        return responses;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getFailureCount() {
        return responses.size() - successCount;
    }

}