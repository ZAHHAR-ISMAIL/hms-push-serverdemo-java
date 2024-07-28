package com.huawei.push.examples;

import java.util.HashMap;

public class PushNotificationDTO {
    private int id;
    private String title;
    private String message;
    private String token;
    private String platform;
    private int priority;
    private HashMap<String, String> data;

    public PushNotificationDTO(int id, String title, String message, String token, String platform, int priority) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.token = token;
        this.platform = platform;
        this.priority = priority;
        this.data = new HashMap<>();
    }

    // Existing getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMsg() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getPlatform() {
        return platform;
    }

    public int getPriority() {
        return priority;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    // Existing setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    // New setter for data
    public void setData(HashMap<String, String> data) {
        this.data = data;
    }
}
