package com.globant.repository;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by efren.campillo on 22/01/16.
 */
public class MainThreadAttacher<TP> {

    Looper mMainLooper;
    Repository mRepository;

    MainThreadAttacher (Looper mainThread, Repository repository) {
        mMainLooper = mainThread;
        mRepository = repository;
    }

    void attachEvent(final RepositoryListener<TP> listener,
                     final NotificationEvent<TP> notificationEvent) {
        if(Thread.currentThread() == mMainLooper.getThread()) {
            deliverEvent(listener, notificationEvent);
        } else {
            Handler handler = new Handler(mMainLooper);
            handler.post(new Runnable() {
                @Override
                public void run() {
                   deliverEvent(listener, notificationEvent);
                }
            });
        }
    }

    private void deliverEvent(RepositoryListener<TP> listener,
                              NotificationEvent<TP> notificationEvent) {

        if (notificationEvent == null) {
            return;
        }

        switch (notificationEvent.mEventType) {
            case RepositoryPendingNotifications.EVENT_ADDED:
                listener.onRetrieved(notificationEvent.mItem);
                break;
            case RepositoryPendingNotifications.EVENT_DELETED:
                listener.onDeleted(notificationEvent.mItem);
                break;
            case RepositoryPendingNotifications.EVENT_UPDATED:
                listener.onUpdate(notificationEvent.mItem);
                break;
            case RepositoryPendingNotifications.EVENT_FAIL:
                listener.onError(notificationEvent.mItem, notificationEvent.mMessage);
                break;
        }
    }
}
