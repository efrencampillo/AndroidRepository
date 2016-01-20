package com.globant.repository;

/**
 * Created by efren.campillo on 20/01/16.
 */
public class NotificationEvent<TP> {
    TP mItem;
    int mEventType;
    String mMessage;

    public NotificationEvent(TP item, int eventType, String message) {
        mItem = item;
        mEventType = eventType;
        mMessage = message;
    }

}