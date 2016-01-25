package com.globant.repository;


import java.util.ArrayList;

public class RepositoryPendingNotifications<TM> {

    private final ArrayList<NotificationEvent<TM>> mPendingNotifications;

    public static final int EVENT_DELETED = 0;
    public static final int EVENT_ADDED = 1;
    public static final int EVENT_UPDATED = 2;
    public static final int EVENT_FAIL = 3;

    RepositoryPendingNotifications() {
        mPendingNotifications = new ArrayList<>();
    }

    NotificationEvent<TM> getNextPendingNotification() {
        if (!mPendingNotifications.isEmpty()) {
            return mPendingNotifications.remove(0);
        }
        return null;
    }

    void addPendingNotification(NotificationEvent notificationEvent) {
        mPendingNotifications.add(notificationEvent);
    }


    void clear() {
        mPendingNotifications.clear();
    }

}
