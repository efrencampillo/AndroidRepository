package com.globant.repository;


import java.util.ArrayList;

public class RepositoryPendingNotifications<TP> {

    private ArrayList<NotificationEvent<TP>> mPendingNotifications;

    public static final int EVENT_DELETED = 0;
    public static final int EVENT_ADDED = 1;
    public static final int EVENT_UPDATED = 2;
    public static final int EVENT_FAIL = 3;

    RepositoryPendingNotifications() {
        mPendingNotifications = new ArrayList<>();
    }

    NotificationEvent<TP> getNextPendingNotification() {
        if (mPendingNotifications.size() > 0) {
            return mPendingNotifications.remove(0);
        }
        return null;
    }

    void addPendingNotification(TP item, int eventType, String message) {
        mPendingNotifications.add(new NotificationEvent<>(item, eventType, message));
    }


    void clear() {
        mPendingNotifications.clear();
    }

}
