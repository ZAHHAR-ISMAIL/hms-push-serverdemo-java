/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.huawei.push.messaging;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.huawei.push.exception.HuaweiMesssagingException;
import com.huawei.push.message.Message;
import com.huawei.push.message.TopicMessage;
import com.huawei.push.model.TopicOperation;
import com.huawei.push.reponse.SendResponse;
import com.huawei.push.util.ValidatorUtils;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.huawei.push.util.ValidatorUtils.checkArgument;

/**
 * This class is the entrance for all server-side HCM actions.
 *
 * <p>You can get a instance of {@link com.huawei.push.messaging.HuaweiMessaging}
 * by a instance of {@link com.huawei.push.messaging.HuaweiApp}, and then use it to send a message
 */
public class HuaweiMessaging {
    private static final Logger logger = LoggerFactory.getLogger(HuaweiMessaging.class);

    static final String INTERNAL_ERROR = "internal error";

    static final String UNKNOWN_ERROR = "unknown error";

    static final String KNOWN_ERROR = "known error";

    private final HuaweiApp app;
    private final Supplier<? extends HuaweiMessageClient> messagingClient;

    private HuaweiMessaging(Builder builder) {
        this.app = builder.app;
        this.messagingClient = Suppliers.memoize(builder.messagingClient);
    }

    /**
     * Gets the {@link HuaweiMessaging} instance for the specified {@link HuaweiApp}.
     *
     * @return The {@link HuaweiMessaging} instance for the specified {@link HuaweiApp}.
     */
    public static synchronized HuaweiMessaging getInstance(HuaweiApp app) {
        HuaweiMessagingService service = ImplHuaweiTrampolines.getService(app, SERVICE_ID, HuaweiMessagingService.class);
        if (service == null) {
            service = ImplHuaweiTrampolines.addService(app, new HuaweiMessagingService(app));
        }
        return service.getInstance();
    }

    private static HuaweiMessaging fromApp(final HuaweiApp app) {
        return HuaweiMessaging.builder()
                .setApp(app)
                .setMessagingClient(() -> HuaweiMessageClientImpl.fromApp(app))
                .build();
    }

    HuaweiMessageClient getMessagingClient() {
        return messagingClient.get();
    }

    /**
     * Sends the given {@link Message} via HCM.
     *
     * @param message A non-null {@link Message} to be sent.
     * @return {@link SendResponse}.
     * @throws HuaweiMesssagingException If an error occurs while handing the message off to HCM for
     *                                   delivery.
     */
    public SendResponse sendMessage(Message message) throws HuaweiMesssagingException {
        return sendMessage(message, false);
    }

    //----------------------------
    // Here we begin
    //---------------------------

    public BatchResponse sendEach(@NonNull List<Message> messages) throws HuaweiMesssagingException {
        return sendEachOp(messages, false).call();
    }



    private CallableOperation<BatchResponse, HuaweiMesssagingException> sendEachOp(
            final List<Message> messages, final boolean dryRun) {
        final List<Message> immutableMessages = ImmutableList.copyOf(messages);
        checkArgument(!immutableMessages.isEmpty(), "messages list must not be empty");
        checkArgument(immutableMessages.size() <= 500,
                "messages list must not contain more than 500 elements");

        return new CallableOperation<BatchResponse, HuaweiMesssagingException>() {
            @Override
            protected BatchResponse execute() throws HuaweiMesssagingException {
                List<ApiFuture<SendResponse>> list = new ArrayList<>();
                for (Message message : immutableMessages) {
                    ApiFuture<SendResponse> messageId = sendOpForSendResponse(message, dryRun).callAsync(app);
                    list.add(messageId);
                }
                try {
                    List<SendResponse> responses = ApiFutures.allAsList(list).get();
                    return new BatchResponseImpl(responses);
                } catch (InterruptedException | ExecutionException e) {
                    throw new HuaweiMesssagingException("CUSTOM_CANCELLED", SERVICE_ID);
                }
            }
        };
    }


    // here try to checl .send or .sendMessage // You can compare Firebase how they send message if they use different to .sendMessage


    private CallableOperation<SendResponse, HuaweiMesssagingException> sendOpForSendResponse(
            final Message message, final boolean dryRun) {
        //checkNotNull(message, "message must not be null");
        final HuaweiMessageClient messagingClient = getMessagingClient();
        return new CallableOperation<SendResponse, HuaweiMesssagingException>() {
//            @Override
            protected SendResponse execute() throws HuaweiMesssagingException {
//                try {

                    return  messagingClient.send(message, false, ImplHuaweiTrampolines.getAccessToken(app));
//                } catch (HuaweiMesssagingException e) {
//                    return ;
//                }
            }
        };
    }



//
//    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//
//    public BatchResponse sendEachOpHms(List<Message> messages, boolean dryRun) throws InterruptedException, ExecutionException {
//        List<Future<SendMessageResult>> futures = new ArrayList<>();
//        for (Message message : messages) {
//            HuaweiMessageFuture future = new HuaweiMessageFuture(message, HuaweiMessageClient, dryRun);
//            futures.add(executorService.submit(future));
//        }
//
//        List<SendMessageResult> successfulResponses = new ArrayList<>();
//        List<Exception> errors = new ArrayList<>();
//        for (Future<SendMessageResult> future : futures) {
//            try {
//                successfulResponses.add(future.get());
//            } catch (ExecutionException e) {
//                errors.add(e.getCause());
//            }
//        }
//
//        executorService.shutdown();
//        return new BatchResponse(successfulResponses, errors);
//    }
//
//
//    public class HuaweiMessageFuture implements Future<SendMessageResult> {
//
//        private final Message message;
//        private final HuaweiMessageClient HuaweiMessageClient;
//        private final boolean dryRun;
//        private SendMessageResult result;
//        private Exception exception;
//
//        public HuaweiMessageFuture(Message message, HuaweiMessageClient HuaweiMessageClient, boolean dryRun) {
//            this.message = message;
//            this.HuaweiMessageClient = HuaweiMessageClient;
//            this.dryRun = dryRun;
//        }
//
//        @Override
//        public boolean cancel(boolean mayInterruptIfRunning) {
//            // Implement cancellation logic if needed
//            return false;
//        }
//
//        @Override
//        public boolean isCancelled() {
//            // Implement cancellation check logic if needed
//            return false;
//        }
//
//        @Override
//        public boolean isDone() {
//            return result != null || exception != null;
//        }
//
//        @Override
//        public SendMessageResult get() throws InterruptedException, ExecutionException {
//            if (!isDone()) {
//                throw new InterruptedException("Future not yet complete");
//            }
//            if (exception != null) {
//                throw new ExecutionException(exception);
//            }
//            return result;
//        }
//
//        @Override
//        public SendMessageResult get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
//            throw new UnsupportedOperationException("get(long, TimeUnit) not supported");
//        }
//
//        public void run() {
//            try {
//                if (dryRun) {
//                    result = new SendMessageResult(message, "Dry Run Successful");
//                } else {
//                    String messageId = sendMessage(message); // Replace with actual method
//                    result = new SendMessageResult(message, messageId);
//                }
//            } catch (Exception e) {
//                exception = e;
//            }
//        }
//    }
//
//


    /**
     * @param topicMessage topicmessage
     * @return topic subscribe response
     * @throws HuaweiMesssagingException
     */
    public SendResponse subscribeTopic(TopicMessage topicMessage) throws HuaweiMesssagingException {
        final HuaweiMessageClient messagingClient = getMessagingClient();
        return messagingClient.send(topicMessage, TopicOperation.SUBSCRIBE.getValue(), ImplHuaweiTrampolines.getAccessToken(app));
    }

    /**
     * @param topicMessage topic Message
     * @return topic unsubscribe response
     * @throws HuaweiMesssagingException
     */
    public SendResponse unsubscribeTopic(TopicMessage topicMessage) throws HuaweiMesssagingException {
        final HuaweiMessageClient messagingClient = getMessagingClient();
        return messagingClient.send(topicMessage, TopicOperation.UNSUBSCRIBE.getValue(), ImplHuaweiTrampolines.getAccessToken(app));
    }

    /**
     * @param topicMessage topic Message
     * @return topic list
     * @throws HuaweiMesssagingException
     */
    public SendResponse listTopic(TopicMessage topicMessage) throws HuaweiMesssagingException {
        final HuaweiMessageClient messagingClient = getMessagingClient();
        return messagingClient.send(topicMessage, TopicOperation.LIST.getValue(), ImplHuaweiTrampolines.getAccessToken(app));
    }


    /**
     * Sends message {@link Message}
     *
     * <p>If the {@code validateOnly} option is set to true, the message will not be actually sent. Instead
     * HCM performs all the necessary validations, and emulates the send operation.
     *
     * @param message      message {@link Message} to be sent.
     * @param validateOnly a boolean indicating whether to send message for test or not.
     * @return {@link SendResponse}.
     * @throws HuaweiMesssagingException exception.
     */
    public SendResponse sendMessage(Message message, boolean validateOnly) throws HuaweiMesssagingException {
        checkArgument(message != null, "message must not be null");
        final HuaweiMessageClient messagingClient = getMessagingClient();
        return messagingClient.send(message, validateOnly, ImplHuaweiTrampolines.getAccessToken(app));
    }

    /**
     * HuaweiMessagingService
     */
    private static final String SERVICE_ID = HuaweiMessaging.class.getName();

    private static class HuaweiMessagingService extends HuaweiService<HuaweiMessaging> {

        HuaweiMessagingService(HuaweiApp app) {
            super(SERVICE_ID, HuaweiMessaging.fromApp(app));
        }

        @Override
        public void destroy() {

        }
    }

    /**
     * Builder for constructing {@link HuaweiMessaging}.
     */
    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private HuaweiApp app;
        private Supplier<? extends HuaweiMessageClient> messagingClient;

        private Builder() {
        }

        public Builder setApp(HuaweiApp app) {
            this.app = app;
            return this;
        }

        public Builder setMessagingClient(Supplier<? extends HuaweiMessageClient> messagingClient) {
            this.messagingClient = messagingClient;
            return this;
        }

        public HuaweiMessaging build() {
            return new HuaweiMessaging(this);
        }
    }
}
