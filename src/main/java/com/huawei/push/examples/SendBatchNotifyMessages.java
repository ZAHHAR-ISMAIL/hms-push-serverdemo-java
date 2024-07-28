package com.huawei.push.examples;

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

import java.util.*;

public class SendBatchNotifyMessages {
    /**
     * send notification message
     *
     * @throws HuaweiMesssagingException
     */

    private static LinkedHashSet<PushNotificationDTO> PNList = new LinkedHashSet<PushNotificationDTO>();

    public void sendNotification() throws HuaweiMesssagingException {
        HuaweiApp app = InitAppUtils.initializeApp();
        HuaweiMessaging huaweiMessaging = HuaweiMessaging.getInstance(app);

        int id1 = 1;
        String title1 = "title1";
        String message1 = "message1";
        String platform1 = "Android";
        String token1 = "IQAAAACy07ONAACUbGA8FAHZV7-6AECUwKUnXoERL-L7N2lhK631y89mxFBOvf10GW7kZDTrOMrBLA0p9wmVyfnpRBfVPbteOCgrjJyIX_4qe02uTw";

        PushNotificationDTO pn1 = new PushNotificationDTO(id1, title1, message1, token1, platform1, 0);
        HashMap<String, String> data1 = new HashMap<String, String>();
        data1.put("title", pn1.getTitle());
        data1.put("body", pn1.getMsg());
        pn1.setData(data1);
        PNList.add(pn1);


        // Object 2
        int id2 = 2;
        String title2 = "title2";
        String message2 = "message2";
        String platform2 = "Android";
        String token2 = "IQAAAACy07ONAADq5Az0KdTm0ehBsYczBYtaeADmoHQXkd1TTI6sWahjWFgsN9VAi_YXMsvDR5Qc1WiYRXD_6teci1h-hCGQxtMKmN1_lv_InP2qPg";

        PushNotificationDTO pn2 = new PushNotificationDTO(id2, title2, message2, token2, platform2, 0);
        HashMap<String, String> data2 = new HashMap<String, String>();
        data2.put("title", pn2.getTitle());
        data2.put("body", pn2.getMsg());
        pn2.setData(data2);
        PNList.add(pn2);


        Iterator<PushNotificationDTO> iter = PNList.iterator();
        List<Message> messages = new ArrayList<>();

        while (iter.hasNext()) {
            // Create the List<Message> of 500 Push Notifications
            PushNotificationDTO pn = iter.next();

            Notification notification = Notification.builder().setTitle(pn.getTitle())
                    .setBody(pn.getMsg())
                    .build();

            JSONObject multiLangKey = new JSONObject();
            JSONObject titleKey = new JSONObject();
            titleKey.put("en", pn.getTitle());
            JSONObject bodyKey = new JSONObject();
            bodyKey.put("en", "My name is %s, I am from %s.");
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
                    .setBigTitle(pn.getTitle())
                    .setBigBody(pn.getMsg())
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

            Message message = Message.builder().setNotification(notification)
                    .setAndroidConfig(androidConfig)
                    .addToken(pn.getToken())
                    .build();

            messages.add(message);

        }

        BatchResponse batchResponse = huaweiMessaging.sendEach(messages);

    }
}