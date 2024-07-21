package com.huawei.push;

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

import com.alibaba.fastjson.JSONObject;
import com.huawei.push.android.AndroidNotification;
import com.huawei.push.android.BadgeNotification;
import com.huawei.push.android.Button;
import com.huawei.push.android.ClickAction;
import com.huawei.push.android.Color;
import com.huawei.push.android.LightSettings;
import com.huawei.push.exception.HuaweiMesssagingException;
import com.huawei.push.message.AndroidConfig;
import com.huawei.push.message.Message;
import com.huawei.push.message.Notification;
import com.huawei.push.messaging.BatchResponse;
import com.huawei.push.messaging.HuaweiApp;
import com.huawei.push.messaging.HuaweiMessaging;
import com.huawei.push.model.Urgency;
import com.huawei.push.model.Importance;
import com.huawei.push.model.Visibility;
import com.huawei.push.reponse.SendResponse;
import com.huawei.push.util.InitAppUtils;

import java.util.ArrayList;
import java.util.List;

public class SendNotifyMessage {
    /**
     * send notification message
     *
     * @throws HuaweiMesssagingException
     */
    public void sendNotification() throws HuaweiMesssagingException {
        HuaweiApp app = InitAppUtils.initializeApp();
        HuaweiMessaging huaweiMessaging = HuaweiMessaging.getInstance(app);

        Notification notification_1 = Notification.builder().setTitle("sample title")
                .setBody("sample message body")
                .build();

        JSONObject multiLangKey = new JSONObject();
        JSONObject titleKey = new JSONObject();
        titleKey.put("en","helloMagic");
        JSONObject bodyKey = new JSONObject();
        bodyKey.put("en","My name is %s, I am from %s.");
        multiLangKey.put("key1", titleKey);
        multiLangKey.put("key2", bodyKey);

        LightSettings lightSettings = LightSettings.builder().setColor(Color.builder().setAlpha(0f).setRed(0f).setBlue(1f).setGreen(1f).build())
                .setLightOnDuration("3.5")
                .setLightOffDuration("5S")
                .build();

        AndroidNotification androidNotification = AndroidNotification.builder().setIcon("/raw/ic_launcher2")
                .setColor("#AACCDD")
                .setSound("/raw/shake")
                .setDefaultSound(true)
                .setTag("tagBoom")
                .setClickAction(ClickAction.builder().setType(2).setUrl("https://www.huawei.com").build())
                .setBodyLocKey("key2")
                .addBodyLocArgs("boy").addBodyLocArgs("dog")
                .setTitleLocKey("key1")
                .addTitleLocArgs("Girl").addTitleLocArgs("Cat")
                .setChannelId("Your Channel ID")
                .setNotifySummary("some summary")
                .setMultiLangkey(multiLangKey)
                .setStyle(1)
                .setBigTitle("Big Boom Title")
                .setBigBody("Big Boom Body")
                .setAutoClear(86400000)
                .setNotifyId(486)
                .setGroup("Group1")
                .setImportance(Importance.LOW.getValue())
                .setLightSettings(lightSettings)
                .setBadge(BadgeNotification.builder().setAddNum(1).setBadgeClass("Classic").build())
                .setVisibility(Visibility.PUBLIC.getValue())
                .setForegroundShow(true)
                .addInboxContent("content1").addInboxContent("content2").addInboxContent("content3").addInboxContent("content4").addInboxContent("content5")
                .addButton(Button.builder().setName("button1").setActionType(0).build())
                .addButton(Button.builder().setName("button2").setActionType(1).setIntentType(0).setIntent("https://com.huawei.hms.hmsdemo/deeplink").build())
                .addButton(Button.builder().setName("button3").setActionType(4).setData("your share link").build())
                .build();

        AndroidConfig androidConfig = AndroidConfig.builder().setCollapseKey(-1)
                .setUrgency(Urgency.HIGH.getValue())
                .setTtl("10000s")
                .setBiTag("the_sample_bi_tag_for_receipt_service")
                .setNotification(androidNotification)
                .build();

        Message message_1 = Message.builder().setNotification(notification_1)
                .setAndroidConfig(androidConfig)
                .addToken("IQAAAACy07ONAACUbGA8FAHZV7-6AECUwKUnXoERL-L7N2lhK631y89mxFBOvf10GW7kZDTrOMrBLA0p9wmVyfnpRBfVPbteOCgrjJyIX_4qe02uTw")
                .build();

        // For testing sendEach
        Message message_2 = Message.builder().setNotification(notification_1)
                .setAndroidConfig(androidConfig)
                .addToken("IQAAAACy07ONAADq5Az0KdTm0ehBsYczBYtaeADmoHQXkd1TTI6sWahjWFgsN9VAi_YXMsvDR5Qc1WiYRXD_6teci1h-hCGQxtMKmN1_lv_InP2qPg")
                .build();

       // SendResponse response = huaweiMessaging.sendMessage(message_1);

        List<Message> messages = new ArrayList<>();

        messages.add(message_1);
        messages.add(message_2);

        BatchResponse batchResponse = huaweiMessaging.sendEach(messages);

    }
}